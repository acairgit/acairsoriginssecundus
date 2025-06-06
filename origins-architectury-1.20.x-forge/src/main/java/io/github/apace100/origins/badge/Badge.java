package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.registry.DataObject;
import io.github.apace100.calio.registry.DataObjectFactory;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface Badge extends DataObject<Badge> {

	ResourceLocation spriteId();

	boolean hasTooltip();

	@OnlyIn(Dist.CLIENT)
	List<ClientTooltipComponent> getTooltipComponents(ConfiguredPower<?, ?> powerType, int widthLimit, float time, Font textRenderer);

	SerializableData.Instance toData(SerializableData.Instance instance);

	BadgeFactory getBadgeFactory();

	@Override
	default DataObjectFactory<Badge> getFactory() {
		return this.getBadgeFactory();
	}

	default void writeBuf(FriendlyByteBuf buf) {
		DataObjectFactory<Badge> factory = this.getFactory();
		buf.writeResourceLocation(this.getBadgeFactory().id());
		factory.getData().write(buf, factory.toData(this));
	}
}
