package io.github.apace100.origins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsBuiltinRegistries;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.S2COpenOriginScreen;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class OriginCommand {

	private enum TargetType {
		INVOKER,
		SPECIFY
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				literal("origin").requires(cs -> cs.hasPermission(2))
						.then(literal("set")
								.then(argument("targets", EntityArgument.players())
										.then(argument("layer", LayerArgumentType.layer())
												.then(argument("origin", OriginArgumentType.origin())
														.executes(OriginCommand::setOrigin))))
						).then(literal("has")
								.then(argument("targets", EntityArgument.players())
										.then(argument("layer", LayerArgumentType.layer())
												.then(argument("origin", OriginArgumentType.origin())
														.executes(OriginCommand::hasOrigin))))
						).then(literal("get")
								.then(argument("target", EntityArgument.player())
										.then(argument("layer", LayerArgumentType.layer())
												.executes(OriginCommand::getOrigin)
										)
								)
						).then(literal("gui")
								.executes(command -> OriginCommand.openMultipleLayerScreens(command, TargetType.INVOKER))
								.then(argument("targets", EntityArgument.players())
										.executes(command -> OriginCommand.openMultipleLayerScreens(command, TargetType.SPECIFY))
										.then(argument("layer", LayerArgumentType.layer())
												.executes(OriginCommand::openSingleLayerScreen)
										)
								)
						).then(literal("random")
								.executes(command -> OriginCommand.randomizeOrigins(command, TargetType.INVOKER))
								.then(argument("targets", EntityArgument.players())
										.executes(command -> OriginCommand.randomizeOrigins(command, TargetType.SPECIFY))
										.then(argument("layer", LayerArgumentType.layer())
												.executes(OriginCommand::randomizeOrigin)
										)
								)
						)
		);
	}

	/**
	 * 	Set the origin of the specified entities in the specified origin layer.
	 * 	@param commandContext the command context
	 * 	@return the number of players whose origin has been set
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is <b>not</b> an instance of {@link ServerPlayer}
	 */
	private static int setOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
		// Sets the origins of several people in the given layer.
		Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
		ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
		ResourceKey<Origin> origin = OriginArgumentType.getOrigin(commandContext, "origin");
		CommandSourceStack serverCommandSource = commandContext.getSource();

		int processedTargets = 0;

		Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
		Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);

		if (
				layerHolder.isPresent() && layerHolder.get().isBound() && originHolder.isPresent() && originHolder.get().isBound() &&
				(origin.equals(OriginRegisters.EMPTY.getKey()) || layerHolder.get().value().contains(origin.location()))
		) {

			for (ServerPlayer target : targets) {
				setOrigin(target, originLayer, origin);
				processedTargets++;
			}


			if (processedTargets == 1) serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.set.success.single", targets.iterator().next().getDisplayName().getString(), layerHolder.get().value().name(), originHolder.get().value().getName()), true);
			else {
                int finalProcessedTargets = processedTargets;
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.set.success.multiple", finalProcessedTargets, layerHolder.get().value().name(), originHolder.get().value().getName()), true);
            }
		}

		else serverCommandSource.sendFailure(Component.translatable("commands.origin.unregistered_in_layer", origin.location(), originLayer.location()));

		return processedTargets;
	}

	private static void setOrigin(Player player, ResourceKey<OriginLayer> layer, ResourceKey<Origin> origin) {
		IOriginContainer.get(player).ifPresent(container -> {
					container.setOrigin(layer, origin);
					container.synchronize();
					container.onChosen(origin, container.hadAllOrigins());
				}
		);
	}

	/**
	 * 	Check if the specified entities has the specified origin in the specified origin layer.
	 * 	@param commandContext the command context
	 * 	@return the number of players that has the specified origin in the specified origin layer
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is <b>not</b> an instance of {@link ServerPlayer}
	 */
	private static int hasOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

		Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
		ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
		ResourceKey<Origin> origin = OriginArgumentType.getOrigin(commandContext, "origin");
		CommandSourceStack serverCommandSource = commandContext.getSource();

		Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
		Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);

		int processedTargets = 0;

		if (
				layerHolder.isPresent() && layerHolder.get().isBound() && originHolder.isPresent() && originHolder.get().isBound() &&
				(origin.equals(OriginRegisters.EMPTY.getKey()) || layerHolder.get().value().contains(origin))
		) {
			for (ServerPlayer target : targets) {
				LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);
				if (originContainer.resolve().isPresent() && (origin.equals(OriginsBuiltinRegistries.ORIGINS.get().getResourceKey(Origin.EMPTY).get()) || originContainer.resolve().get().hasOrigin(originLayer)) && hasOrigin(target, originLayer, origin))
					processedTargets++;
			}

			if (processedTargets == 0)
				serverCommandSource.sendFailure(Component.translatable("commands.execute.conditional.fail"));
			else if (processedTargets == 1)
				serverCommandSource.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), true);
			else {
                int finalProcessedTargets = processedTargets;
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", finalProcessedTargets), true);
            }
		}
		else serverCommandSource.sendFailure(Component.translatable("commands.origin.unregistered_in_layer", origin.location(), originLayer.location()));

		return processedTargets;
	}

	private static boolean hasOrigin(Player player, ResourceKey<OriginLayer> layer, ResourceKey<Origin> origin) {
		return IOriginContainer.get(player).map(x -> Objects.equals(x.getOrigin(layer), origin)).orElse(false);
	}

	/**
	 * 	Get the origin of the specified entity from the specified origin layer.
	 * 	@param commandContext the command context
	 * 	@return 1
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is <b>not</b> an instance of {@link ServerPlayer}
	 */
	private static int getOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

		ServerPlayer target = EntityArgument.getPlayer(commandContext, "target");
		ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
		CommandSourceStack serverCommandSource = commandContext.getSource();

		Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);

		IOriginContainer.get(target).ifPresent(container -> {
			ResourceKey<Origin> origin = container.getOrigin(originLayer);
			Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);
			serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.get.result", target.getDisplayName().getString(), layerHolder.get().value().name(), originHolder.get().value().getName(), origin.location()), true);
		});

		return 1;

	}

	/**
	 * 	Open the 'Choose Origin' screen for the specified origin layer to the specified entities.
	 * 	@param commandContext the command context
	 * 	@return the number of players that had the 'Choose Origin' screen opened for them
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is not an instance of {@link ServerPlayer}
	 */
	private static int openSingleLayerScreen(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

		CommandSourceStack serverCommandSource = commandContext.getSource();
		Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
		ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");

		OriginsAPI.getLayersRegistry().getHolder(originLayer).ifPresent(holder -> {

			for (ServerPlayer target : targets) {
				openLayerScreen(target, List.of(holder));
			}

			serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.gui.layer", targets.size(), holder.value().name()), true);

		});


		return targets.size();

	}


	/**
	 * 	Open the 'Choose Origin' screen for all the enabled origin layers to the specified entities.
	 * 	@param commandContext the command context
	 * 	@return the number of players that had the 'Choose Origin' screen opened for them
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is not an instance of {@link ServerPlayer}
	 */
	private static int openMultipleLayerScreens(CommandContext<CommandSourceStack> commandContext, TargetType targetType) throws CommandSyntaxException {

		CommandSourceStack serverCommandSource = commandContext.getSource();
		List<ServerPlayer> targets = new ArrayList<>();
		List<Holder<OriginLayer>> originLayers = OriginsAPI.getActiveLayers().stream().map(reference -> (Holder<OriginLayer>)reference).toList();

		switch (targetType) {
			case INVOKER -> targets.add(serverCommandSource.getPlayerOrException());
			case SPECIFY -> targets.addAll(EntityArgument.getPlayers(commandContext, "targets"));
		}

		for (ServerPlayer target : targets) {
			openLayerScreen(target, originLayers);
		}

		serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.gui.all", targets.size()), false);
		return targets.size();

	}

	/**
	 * 	Randomize the origin of the specified entities in the specified origin layer.
	 * 	@param commandContext the command context
	 * 	@return the number of players that had their origin randomized in the specified origin layer
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is not an instance of {@link ServerPlayer}
	 */
	private static int randomizeOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {

		CommandSourceStack serverCommandSource = commandContext.getSource();
		Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
		ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");


		Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);;

		if (layerHolder.isPresent() && layerHolder.get().isBound()) {

			if (layerHolder.get().value().allowRandom()) {
				Holder<Origin> origin = null;
				for (ServerPlayer target : targets) {
					origin = getRandomOrigin(target, layerHolder.get());
				}

				if (targets.size() > 1) serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.success.multiple", targets.size(), layerHolder.get().value().name()), true);
				else if (targets.size() == 1) {
                    Holder<Origin> finalOrigin = origin;
                    serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.success.single", targets.iterator().next().getDisplayName().getString(), finalOrigin.value().getName(), layerHolder.get().value().name()), false);
                }

				return targets.size();
			}
			else {
				serverCommandSource.sendFailure(Component.translatable("commands.origin.random.not_allowed", layerHolder.get().value().name()));
				return 0;
			}
		}

		return 0;
	}

	/**
	 * 	Randomize the origins of the specified entities in all of the origin layers that allows to be randomized.
	 * 	@param commandContext the command context
	 * 	@return the number of players that had their origins randomized in all of the origin layers that allows to be randomized
	 * 	@throws CommandSyntaxException if the entity is not found or if the entity is not an instance of {@link ServerPlayer}
	 */
	private static int randomizeOrigins(CommandContext<CommandSourceStack> commandContext, TargetType targetType) throws CommandSyntaxException {

		CommandSourceStack serverCommandSource = commandContext.getSource();
		List<ServerPlayer> targets = new ArrayList<>();
		List<Holder.Reference<OriginLayer>> originLayers = OriginsAPI.getActiveLayers().stream().filter(originLayerReference -> originLayerReference.isBound() && originLayerReference.value().allowRandom()).toList();

		switch (targetType) {
			case INVOKER -> targets.add(serverCommandSource.getPlayerOrException());
			case SPECIFY -> targets.addAll(EntityArgument.getPlayers(commandContext, "targets"));
		}

		for (ServerPlayer target : targets) {
			for (Holder.Reference<OriginLayer> originLayer : originLayers) {
				getRandomOrigin(target, originLayer);
			}
		}

		serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.all", targets.size(), originLayers.size()), false);
		return targets.size();

	}

	private static void openLayerScreen(ServerPlayer target, List<Holder<OriginLayer>> originLayers) {

		LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);
		originContainer.ifPresent(container -> {
			for (Holder<OriginLayer> layer : originLayers) {
				container.setOrigin(layer.unwrapKey().orElseThrow(), OriginRegisters.EMPTY.getKey());
			}
			container.synchronize();
			container.checkAutoChoosingLayers(false);
			OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> target), new S2COpenOriginScreen(false));
		});

	}

	private static Holder<Origin> getRandomOrigin(ServerPlayer target, Holder<OriginLayer> originLayer) {


		List<Holder<Origin>> origins = originLayer.value().randomOrigins(target);
		Holder<Origin> origin = origins.get(new Random().nextInt(origins.size()));
		LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);

		originContainer.ifPresent(container -> {

			boolean hadOriginBefore = container.hadAllOrigins();
			boolean hadAllOrigins = container.hasAllOrigins();

			container.setOrigin(originLayer, origin);
			container.checkAutoChoosingLayers(false);
			container.synchronize();

			if (container.hasAllOrigins() && !hadAllOrigins) container.onChosen(hadOriginBefore);

			if (origin.unwrapKey().isEmpty() || originLayer.unwrapKey().isEmpty()) return;

			Origins.LOGGER.info(
					"Player {} was randomly assigned the origin {} for layer {}",
					target.getDisplayName().getString(),
					origin.unwrapKey().get().location(),
					originLayer.unwrapKey().get().location()
			);

		});

		return origin;

	}
}
