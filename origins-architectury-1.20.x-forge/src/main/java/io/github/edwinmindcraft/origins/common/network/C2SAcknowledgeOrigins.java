package io.github.edwinmindcraft.origins.common.network;

import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SAcknowledgeOrigins {
	public static C2SAcknowledgeOrigins decode(FriendlyByteBuf buf) {
		return new C2SAcknowledgeOrigins();
	}

	public C2SAcknowledgeOrigins() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> IOriginContainer.get(contextSupplier.get().getSender()).ifPresent(IOriginContainer::validateSynchronization));
		contextSupplier.get().setPacketHandled(true);
	}
}
