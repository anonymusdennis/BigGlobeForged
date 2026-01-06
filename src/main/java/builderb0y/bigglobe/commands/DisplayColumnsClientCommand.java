package builderb0y.bigglobe.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientCommandSourceStack;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;

import builderb0y.bigglobe.BigGlobeMod;
import builderb0y.bigglobe.chunkgen.BigGlobeScriptedChunkGenerator;

@OnlyIn(Dist.CLIENT)
public class DisplayColumnsClientCommand {

	public static void register(CommandDispatcher<ClientCommandSourceStack> dispatcher) {
		dispatcher.register(
			CommandManager
			.literal(BigGlobeMod.MODID + ":displayColumns")
			.requires((ClientCommandSourceStack source) -> getGenerator(source) != null)
			.executes((CommandContext<ClientCommandSourceStack> context) -> {
				BigGlobeScriptedChunkGenerator generator = getGenerator(context.getSource());
				if (generator != null) {
					generator.setDisplay(null);
					return 1;
				}
				else {
					return 0;
				}
			})
			.then(
				CommandManager
				.argument("filter", StringArgumentType.greedyString())
				.executes((CommandContext<ClientCommandSourceStack> context) -> {
					BigGlobeScriptedChunkGenerator generator = getGenerator(context.getSource());
					if (generator != null) {
						generator.setDisplay(context.getArgument("filter", String.class));
						return 1;
					}
					else {
						return 0;
					}
				})
			)
		);
	}

	public static @Nullable BigGlobeScriptedChunkGenerator getGenerator(ClientCommandSourceStack source) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.getServer() == null || client.world == null) return null;
		ServerWorld world = client.getServer().getWorld(client.world.getRegistryKey());
		if (world == null) return null;
		return world.getChunkManager().getChunkGenerator() instanceof BigGlobeScriptedChunkGenerator generator ? generator : null;
	}
}