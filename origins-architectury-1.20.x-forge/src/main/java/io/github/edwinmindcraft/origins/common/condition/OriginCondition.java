package io.github.edwinmindcraft.origins.common.condition;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.common.condition.configuration.OriginConfiguration;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class OriginCondition extends EntityCondition<OriginConfiguration> {
	public OriginCondition() {
		super(OriginConfiguration.CODEC);
	}

	@Override
	public boolean check(@NotNull OriginConfiguration configuration, @NotNull Entity entity) {
		return IOriginContainer.get(entity).resolve().map(container -> {
			if (!configuration.origin().isBound())
				return false;
			if (configuration.layer() != null && configuration.layer().isBound())
				return configuration.layer().unwrapKey().isPresent() && configuration.origin().is(container.getOrigin(configuration.layer().unwrapKey().get()));
			return container.getOrigins().values().stream().anyMatch(configuration.origin()::is);
		}).orElse(false);
	}
}
