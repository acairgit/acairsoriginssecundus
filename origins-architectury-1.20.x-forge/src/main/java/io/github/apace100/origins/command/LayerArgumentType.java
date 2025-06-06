package io.github.apace100.origins.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class LayerArgumentType implements ArgumentType<ResourceLocation> {
	public static final DynamicCommandExceptionType LAYER_NOT_FOUND = new DynamicCommandExceptionType((p_208663_0_) -> Component.translatable("commands.origin.layer_not_found", p_208663_0_));

	public static LayerArgumentType layer() {
		return new LayerArgumentType();
	}

	public ResourceLocation parse(StringReader p_parse_1_) throws CommandSyntaxException {
		return ResourceLocation.read(p_parse_1_);
	}

	public static ResourceKey<OriginLayer> getLayer(CommandContext<CommandSourceStack> context, String argumentName) throws CommandSyntaxException {
		ResourceLocation id = context.getArgument(argumentName, ResourceLocation.class);
		if (!OriginsAPI.getLayersRegistry(context.getSource().getServer()).containsKey(id))
			throw LAYER_NOT_FOUND.create(id);
		return ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, id);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(OriginsAPI.getLayersRegistry().keySet(), builder);
	}
}