package io.github.apace100.origins.component;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

@Deprecated
public interface OriginComponent {

	@Deprecated
	boolean hasOrigin(OriginLayer layer);

	@Deprecated
	boolean hasAllOrigins();

	@Deprecated
	HashMap<OriginLayer, Origin> getOrigins();

	@Deprecated
	Origin getOrigin(OriginLayer layer);

	@Deprecated
	boolean hadOriginBefore();

	@Deprecated
	void setOrigin(OriginLayer layer, Origin origin);

	@Deprecated
	void sync();

	@Deprecated(forRemoval = true)
	default void onPowersRead() {}

	@Deprecated
	static void sync(Player player) {
		IOriginContainer.get(player).ifPresent(IOriginContainer::synchronize);
	}

	@Deprecated
	static void onChosen(Player player, boolean hadOriginBefore) {
		IOriginContainer.get(player).ifPresent(x -> x.onChosen(hadOriginBefore));
	}

	@Deprecated
	static void partialOnChosen(Player player, boolean hadOriginBefore, Origin origin) {
		IOriginContainer.get(player).ifPresent(x -> x.onChosen(OriginsAPI.getOriginsRegistry().getResourceKey(origin.getWrapped()).orElseThrow(), hadOriginBefore));
	}

	@Deprecated
	default boolean checkAutoChoosingLayers(Player player, boolean includeDefaults) {
		return IOriginContainer.get(player).map(x -> x.checkAutoChoosingLayers(includeDefaults)).orElse(false);
	}
}
