package io.github.apace100.origins.content;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.S2COpenOriginScreen;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrbOfOriginItem extends Item {

	public OrbOfOriginItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	@NotNull
	public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			IOriginContainer.get(player).ifPresent(container -> {
				Map<OriginLayer, Origin> targets = this.getTargets(stack);
				if (targets.size() > 0) {
					for (Map.Entry<OriginLayer, Origin> target : targets.entrySet()) {
						container.setOrigin(target.getKey(), target.getValue());
					}
				} else {
					for (Holder.Reference<OriginLayer> layer : OriginsAPI.getActiveLayers()) {
						container.setOrigin(layer.key(), Objects.requireNonNull(OriginRegisters.EMPTY.getKey()));
					}
				}
				if (player instanceof ServerPlayer sp) {
					container.checkAutoChoosingLayers(false);
					PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> sp);
					OriginsCommon.CHANNEL.send(target, container.getSynchronizationPacket());
					OriginsCommon.CHANNEL.send(target, new S2COpenOriginScreen(false));
					container.synchronize();
				}
			});
		}
		if (!player.isCreative()) {
			stack.shrink(1);
		}
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flags) {
		Map<OriginLayer, Origin> targets = this.getTargets(stack);
		for (Map.Entry<OriginLayer, Origin> entry : targets.entrySet()) {
			if (entry.getValue() == Origin.EMPTY)
				components.add(Component.translatable("item.origins.orb_of_origin.layer_generic", entry.getKey().name()).withStyle(ChatFormatting.GRAY));
			else
				components.add(Component.translatable("item.origins.orb_of_origin.layer_specific", entry.getKey().name(), entry.getValue().getName()).withStyle(ChatFormatting.GRAY));
		}
	}

	private Map<OriginLayer, Origin> getTargets(ItemStack stack) {
		Map<OriginLayer, Origin> targets = new HashMap<>();
		if (!stack.hasTag()) {
			return targets;
		}
		CompoundTag nbt = Objects.requireNonNull(stack.getTag());
		ListTag targetList = nbt.getList("Targets", Tag.TAG_COMPOUND);
		for (Tag nbtElement : targetList) {
			CompoundTag targetNbt = (CompoundTag) nbtElement;
			if (targetNbt.contains("Layer", Tag.TAG_STRING)) {
				try {
					ResourceLocation id = new ResourceLocation(targetNbt.getString("Layer"));
					OriginLayer layer = OriginsAPI.getLayersRegistry().get(id);
					if (layer == null) continue;
					Origin origin = Origin.EMPTY;
					ResourceLocation originId = null;
					if (targetNbt.contains("Origin", Tag.TAG_STRING)) {
						originId = new ResourceLocation(targetNbt.getString("Origin"));
						origin = OriginsAPI.getOriginsRegistry().get(originId);
					}
					if (origin == null || originId == null)
						continue;
					if (layer.enabled() && (layer.contains(originId) || origin.isSpecial())) {
						targets.put(layer, origin);
					}
				} catch (Exception e) {
					// no op
				}
			}
		}
		return targets;
	}
}
