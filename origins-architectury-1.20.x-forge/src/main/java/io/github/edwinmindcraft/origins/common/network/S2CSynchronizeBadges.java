package io.github.edwinmindcraft.origins.common.network;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import io.github.apace100.origins.badge.Badge;
import io.github.apace100.origins.badge.BadgeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public record S2CSynchronizeBadges(Multimap<ResourceLocation, Badge> badges) {
	public static S2CSynchronizeBadges decode(FriendlyByteBuf buf) {
		Multimap<ResourceLocation, Badge> badges = LinkedListMultimap.create();
		int size = buf.readVarInt();
		for (int i = 0; i < size; i++) {
			ResourceLocation rl = buf.readResourceLocation();
			int count = buf.readVarInt();
			for (int j = 0; j < count; j++)
				badges.put(rl, BadgeManager.REGISTRY.receiveDataObject(buf));
		}
		return new S2CSynchronizeBadges(badges);
	}

	public void encode(FriendlyByteBuf buf) {
		Map<ResourceLocation, Collection<Badge>> map = this.badges().asMap();
		buf.writeVarInt(map.size());
		for (Map.Entry<ResourceLocation, Collection<Badge>> entry : map.entrySet()) {
			buf.writeResourceLocation(entry.getKey());
			buf.writeVarInt(entry.getValue().size());
			for (Badge badge : entry.getValue())
				BadgeManager.REGISTRY.writeDataObject(buf, badge);
		}
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> {
			BadgeManager.clear();
			this.badges.forEach(BadgeManager::putPowerBadge);
		});
		contextSupplier.get().setPacketHandled(true);
	}
}
