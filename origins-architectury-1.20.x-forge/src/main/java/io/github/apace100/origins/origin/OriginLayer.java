package io.github.apace100.origins.origin;

import com.google.common.collect.ImmutableList;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Use {@link io.github.edwinmindcraft.origins.api.origin.OriginLayer} where possible instead.
 */
@Deprecated
public class OriginLayer implements Comparable<OriginLayer> {
	private final io.github.edwinmindcraft.origins.api.origin.OriginLayer wrapped;

	public OriginLayer(io.github.edwinmindcraft.origins.api.origin.OriginLayer wrapped) {this.wrapped = wrapped;}

	@Deprecated
	public String getOrCreateTranslationKey() {
		return this.wrapped.name() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	public io.github.edwinmindcraft.origins.api.origin.OriginLayer getWrapped() {
		return this.wrapped;
	}

	@Deprecated
	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}

	@Deprecated
	public String getMissingOriginNameTranslationKey() {
		return this.wrapped.missingName() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public String getMissingOriginDescriptionTranslationKey() {
		return this.wrapped.missingDescription() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public String getTitleViewOriginTranslationKey() {
		return this.wrapped.title().view() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public boolean shouldOverrideViewOriginTitle() {
		return this.wrapped.title().view() != null;
	}

	@Deprecated
	public String getTitleChooseOriginTranslationKey() {
		return this.wrapped.title().choose() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public boolean shouldOverrideChooseOriginTitle() {
		return this.wrapped.title().choose() != null;
	}

	@Deprecated
	public ResourceLocation getIdentifier() {
		return OriginsAPI.getLayersRegistry().getKey(this.wrapped);
	}

	@Deprecated
	public boolean isEnabled() {
		return this.wrapped.enabled();
	}

	@Deprecated
	public boolean hasDefaultOrigin() {
		return this.wrapped.hasDefaultOrigin();
	}

	@Deprecated
	public ResourceLocation getDefaultOrigin() {
		return this.wrapped.defaultOrigin().unwrapKey().map(ResourceKey::location).orElse(null);
	}

	@Deprecated
	public boolean shouldAutoChoose() {
		return this.wrapped.autoChoose();
	}

	@Deprecated
	public List<ResourceLocation> getOrigins() {
		return this.wrapped.origins().stream().map(Holder::unwrapKey).flatMap(Optional::stream).map(ResourceKey::location).collect(ImmutableList.toImmutableList());
	}

	@Deprecated
	public List<ResourceLocation> getOrigins(Player playerEntity) {
		return this.wrapped.origins(playerEntity).stream().map(Holder::unwrapKey).flatMap(Optional::stream).map(ResourceKey::location).collect(ImmutableList.toImmutableList());
	}

	@Deprecated
	public int getOriginOptionCount(Player playerEntity) {
		return this.wrapped.getOriginOptionCount(playerEntity);
	}

	@Deprecated
	public boolean contains(Origin origin) {
		return this.wrapped.contains(origin.getIdentifier());
	}

	@Deprecated
	public boolean contains(Origin origin, Player playerEntity) {
		return this.wrapped.contains(origin.getIdentifier(), playerEntity);
	}

	@Deprecated
	public boolean isRandomAllowed() {
		return this.wrapped.allowRandom();
	}

	@Deprecated
	public boolean isHidden() {
		return this.wrapped.hidden();
	}

	@Deprecated
	public List<ResourceLocation> getRandomOrigins(Player playerEntity) {
		return this.wrapped.randomOrigins(playerEntity).stream().map(Holder::unwrapKey).flatMap(Optional::stream).map(ResourceKey::location).collect(ImmutableList.toImmutableList());
	}

	@Override
	public int hashCode() {
		return this.wrapped.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		else if (!(obj instanceof OriginLayer)) return false;
		else return Objects.equals(this.wrapped, ((OriginLayer) obj).wrapped);
	}

	@Override
	public int compareTo(OriginLayer o) {
		return this.wrapped.compareTo(o.wrapped);
	}
}
