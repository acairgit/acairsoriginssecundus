package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public record TooltipBadge(ResourceLocation spriteId, Component text) implements Badge {

	public TooltipBadge(SerializableData.Instance instance) {
		this(instance.getId("sprite"), instance.get("text"));
	}

	@Override
	public boolean hasTooltip() {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	public static void addLines(List<ClientTooltipComponent> tooltips, Component text, Font textRenderer, int widthLimit) {
		if (textRenderer.width(text) > widthLimit) {
			for (FormattedCharSequence orderedText : textRenderer.split(text, widthLimit)) {
				tooltips.add(new ClientTextTooltip(orderedText));
			}
		} else {
			tooltips.add(new ClientTextTooltip(text.getVisualOrderText()));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<ClientTooltipComponent> getTooltipComponents(ConfiguredPower<?, ?> powerType, int widthLimit, float time, Font textRenderer) {
		List<ClientTooltipComponent> tooltips = new LinkedList<>();
		addLines(tooltips, this.text, textRenderer, widthLimit);
		return tooltips;
	}

	@Override
	public SerializableData.Instance toData(SerializableData.Instance instance) {
		instance.set("sprite", this.spriteId);
		instance.set("text", this.text);
		return instance;
	}

	@Override
	public BadgeFactory getBadgeFactory() {
		return BadgeFactories.TOOLTIP.get();
	}
}
