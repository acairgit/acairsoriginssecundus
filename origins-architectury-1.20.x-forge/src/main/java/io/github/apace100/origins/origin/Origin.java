package io.github.apace100.origins.origin;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Deprecated
public class Origin {

    /*public static final SerializableData DATA = new SerializableData()
        .add("powers", SerializableDataTypes.IDENTIFIERS, Lists.newArrayList())
        .add("icon", CompatibilityDataTypes.ITEM_OR_ITEM_STACK, new ItemStack(Items.AIR))
        .add("unchoosable", SerializableDataTypes.BOOLEAN, false)
        .add("order", SerializableDataTypes.INT, Integer.MAX_VALUE)
        .add("impact", OriginsDataTypes.IMPACT, Impact.NONE)
        .add("loading_priority", SerializableDataTypes.INT, 0)
        .add("upgrades", OriginsDataTypes.UPGRADES, null)
        .add("name", SerializableDataTypes.STRING, "")
        .add("description", SerializableDataTypes.STRING, "");*/

	public static final Origin EMPTY;

	static {
		EMPTY = new Origin(OriginRegisters.EMPTY);
	}

	@Deprecated
	public static void init() {}


	@Deprecated
	public static Map<OriginLayer, Origin> get(Entity entity) {
		if (entity instanceof Player) {
			return get((Player) entity);
		}
		return new HashMap<>();
	}

	@Deprecated
	public static Map<OriginLayer, Origin> get(Player player) {
		return IOriginContainer.get(player).map(IOriginContainer::asLegacyComponent).map(OriginComponent::getOrigins).orElseGet(HashMap::new);
	}

	private final Lazy<io.github.edwinmindcraft.origins.api.origin.Origin> wrapped;

	public Origin(Supplier<io.github.edwinmindcraft.origins.api.origin.Origin> wrapped) {
		this.wrapped = Lazy.of(wrapped);
	}

	public io.github.edwinmindcraft.origins.api.origin.Origin getWrapped() {
		return this.wrapped.get();
	}

	@Deprecated
	public boolean hasUpgrade() {
		return !this.getWrapped().getUpgrades().isEmpty();
	}

	@Deprecated
	public Optional<OriginUpgrade> getUpgrade(Advancement advancement) {
		return this.getWrapped().findUpgrade(advancement.getId()).map(OriginUpgrade::new);
	}

	@Deprecated
	public ResourceLocation getIdentifier() {
		return OriginsAPI.getOriginsRegistry().getKey(this.getWrapped());
	}

/*	public boolean hasPowerType(PowerType<?> powerType) {
		if (powerType.getIdentifier() == null) {
			return false;
		}
		if (this.powerTypes.contains(powerType)) {
			return true;
		}
		for (PowerType<?> pt : this.powerTypes) {
			if (pt instanceof MultiplePowerType) {
				if (((MultiplePowerType<?>) pt).getSubPowers().contains(powerType.getIdentifier())) {
					return true;
				}
			}
		}
		return false;
	}*/

	@Deprecated
	public void removePowerType(PowerType<?> powerType) {
		throw new UnsupportedOperationException("Origins are immutable in forge.");
		//this.powerTypes.remove(powerType);
	}


	@Deprecated
	public boolean isSpecial() {
		return this.getWrapped().isSpecial();
	}

	@Deprecated
	public boolean isChoosable() {
		return this.getWrapped().isChoosable();
	}

/*	public Iterable<PowerType<?>> getPowerTypes() {
		return this.powerTypes;
	}*/

	@Deprecated
	public Impact getImpact() {
		return this.getWrapped().getImpact();
	}

	@Deprecated
	public ItemStack getDisplayItem() {
		return this.getWrapped().getIcon();
	}

	@Deprecated
	public String getOrCreateNameTranslationKey() {
		return this.getWrapped().getName() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public Component getName() {
		return this.getWrapped().getName();
	}

	@Deprecated
	public String getOrCreateDescriptionTranslationKey() {
		return this.getWrapped().getDescription() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc ? tc.getKey() : "";
	}

	@Deprecated
	public Component getDescription() {
		return this.getWrapped().getDescription();
	}

	@Deprecated
	public int getOrder() {
		return this.getWrapped().getOrder();
	}

	@Override
	public String toString() {
		return this.getWrapped().toString();
	}

	@Override
	public int hashCode() {
		return this.getWrapped().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Origin other)
			return Objects.equals(this.getWrapped(), other.getWrapped());
		return false;
	}
}
