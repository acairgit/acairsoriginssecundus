package io.github.apace100.origins.badge;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.registry.DataObjectFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Function;

//FORGE: Properly back factories with a forge registry.
public final class BadgeFactory implements DataObjectFactory<Badge> {
	private final ResourceLocation id;
	private final SerializableData data;
	private final Function<SerializableData.Instance, Badge> factory;
	private final Codec<Badge> codec;

	public BadgeFactory(ResourceLocation id, SerializableData data, Function<SerializableData.Instance, Badge> factory) {
		this.id = id;
		this.data = data;
		this.factory = factory;
		//TODO: Full codec backing for badges.
		this.codec = data.xmap(factory, x -> x.toData(this.data().new Instance())).codec();
	}

	public Codec<Badge> getCodec() {
		return this.codec;
	}

	@Override
	public SerializableData getData() {
		return this.data;
	}

	@Override
	public Badge fromData(SerializableData.Instance instance) {
		return this.factory().apply(instance);
	}

	@Override
	public SerializableData.Instance toData(Badge badge) {
		return badge.toData(this.data.new Instance());
	}

	public ResourceLocation id() {
		return this.id;
	}

	public SerializableData data() {
		return this.data;
	}

	public Function<SerializableData.Instance, Badge> factory() {
		return this.factory;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BadgeFactory) obj;
		return Objects.equals(this.id, that.id) &&
			   Objects.equals(this.data, that.data) &&
			   Objects.equals(this.factory, that.factory);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.data, this.factory);
	}

	@Override
	public String toString() {
		return "BadgeFactory[" +
			   "id=" + this.id + ", " +
			   "data=" + this.data + ", " +
			   "factory=" + this.factory + ']';
	}
}