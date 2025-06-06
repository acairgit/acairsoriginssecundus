package io.github.apace100.origins.power;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.origins.api.origin.IOriginCallbackPower;
import io.github.edwinmindcraft.origins.common.power.configuration.OriginsCallbackConfiguration;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class OriginsCallbackPower extends PowerFactory<OriginsCallbackConfiguration> implements IOriginCallbackPower<OriginsCallbackConfiguration> {
	public OriginsCallbackPower() {
		super(OriginsCallbackConfiguration.CODEC);
	}

	@Override
	protected void onGained(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionGained(), player);
	}

	@Override
	protected void onLost(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionLost(), player);
	}

	@Override
	protected void onAdded(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionAdded(), player);
	}

	@Override
	protected void onRemoved(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRemoved(), player);
	}

	@Override
	protected void onRespawn(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity player) {
		ConfiguredEntityAction.execute(configuration.entityActionRespawned(), player);
	}

	@Override
	public void onChosen(@NotNull OriginsCallbackConfiguration configuration, @NotNull Entity entity, boolean isOrb) {
		if (!isOrb || configuration.onOrb())
			ConfiguredEntityAction.execute(configuration.entityActionChosen(), entity);
	}
}
