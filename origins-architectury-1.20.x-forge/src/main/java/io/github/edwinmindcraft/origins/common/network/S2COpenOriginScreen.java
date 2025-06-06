package io.github.edwinmindcraft.origins.common.network;

import io.github.edwinmindcraft.origins.client.OriginsClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2COpenOriginScreen(boolean showDirtBackground) {
	public static S2COpenOriginScreen decode(FriendlyByteBuf buf) {
		return new S2COpenOriginScreen(buf.readBoolean());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(this.showDirtBackground());
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			OriginsClient.AWAITING_DISPLAY.set(true);
			OriginsClient.SHOW_DIRT_BACKGROUND = this.showDirtBackground();
		}));
		contextSupplier.get().setPacketHandled(true);
	}
}
