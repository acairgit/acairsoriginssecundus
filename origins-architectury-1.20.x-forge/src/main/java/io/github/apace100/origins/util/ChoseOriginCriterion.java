package io.github.apace100.origins.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChoseOriginCriterion extends SimpleCriterionTrigger<ChoseOriginCriterion.Conditions> {

	public static final ChoseOriginCriterion INSTANCE = new ChoseOriginCriterion();

	private static final ResourceLocation ID = new ResourceLocation(Origins.MODID, "chose_origin");

	@Override
	protected @NotNull Conditions createInstance(@NotNull JsonObject obj, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext predicateDeserializer) {
		ResourceLocation id = ResourceLocation.tryParse(GsonHelper.getAsString(obj, "origin"));
		return new Conditions(playerPredicate, id);
	}

	public void trigger(ServerPlayer player, ResourceKey<Origin> origin) {
		this.trigger(player, (conditions -> conditions.matches(origin)));
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final ResourceLocation originId;

		public Conditions(ContextAwarePredicate player, ResourceLocation originId) {
			super(ChoseOriginCriterion.ID, player);
			this.originId = originId;
		}

		public boolean matches(ResourceKey<Origin> origin) {
			return Objects.equals(origin.location(), this.originId);
		}

		public @NotNull JsonObject serializeToJson(@NotNull SerializationContext predicateSerializer) {
			JsonObject jsonObject = super.serializeToJson(predicateSerializer);
			jsonObject.add("origin", new JsonPrimitive(this.originId.toString()));
			return jsonObject;
		}
	}
}
