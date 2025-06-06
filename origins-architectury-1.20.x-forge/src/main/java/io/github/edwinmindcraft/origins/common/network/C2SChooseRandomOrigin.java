package io.github.edwinmindcraft.origins.common.network;

import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Supplier;

public record C2SChooseRandomOrigin(ResourceLocation layer) {
	public static C2SChooseRandomOrigin decode(FriendlyByteBuf buf) {
		return new C2SChooseRandomOrigin(buf.readResourceLocation());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeResourceLocation(this.layer());
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> {
			ServerPlayer sender = contextSupplier.get().getSender();
			if (sender == null) return;
			IOriginContainer.get(sender).ifPresent(container -> {
				Optional<Holder.Reference<OriginLayer>> layer = OriginsAPI.getLayersRegistry().getHolder(ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, this.layer())).filter(Holder::isBound);
				if (layer.isEmpty()) {
					Origins.LOGGER.warn("Player {} tried to select a random origin for missing layer {}", sender.getScoreboardName(), this.layer());
					return;
				}
				if (container.hasAllOrigins() || container.hasOrigin(layer.get())) {
					Origins.LOGGER.warn("Player {} tried to choose origin for layer {} while having one already.", sender.getScoreboardName(), this.layer());
					return;
				}
				Optional<Holder<Origin>> selected = layer.get().value().selectRandom(sender);
				if (!layer.get().value().allowRandom() || selected.isEmpty()) {
					Origins.LOGGER.warn("Player {} tried to choose a random Origin for layer {}, which is not allowed!", sender.getScoreboardName(), this.layer());
					container.setOrigin(layer.get(), OriginRegisters.EMPTY.getHolder().orElseThrow());
					return;
				}
				Holder<Origin> origin = selected.get();
				boolean hadOriginBefore = container.hadAllOrigins();
				boolean hadAllOrigins = container.hasAllOrigins();
				container.setOrigin(layer.get(), origin);
				container.checkAutoChoosingLayers(false);
				container.synchronize();
				if (container.hasAllOrigins() && !hadAllOrigins)
					container.onChosen(hadOriginBefore);
				Origins.LOGGER.info("Player {} was randomly assigned the following Origin: \"{}\" for layer: {}", sender.getScoreboardName(), origin.unwrapKey().orElse(null), this.layer());
				OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new S2CConfirmOrigin(this.layer(), origin.unwrapKey().map(ResourceKey::location).orElse(null)));
			});
		});
		contextSupplier.get().setPacketHandled(true);
	}
}
