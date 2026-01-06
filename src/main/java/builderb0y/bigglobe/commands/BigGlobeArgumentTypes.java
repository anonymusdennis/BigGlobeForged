package builderb0y.bigglobe.commands;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import builderb0y.bigglobe.BigGlobeMod;
import builderb0y.bigglobe.commands.EnumArgument.EnumArgumentSerializer;

public class BigGlobeArgumentTypes {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void init() {
		BigGlobeMod.LOGGER.debug("Registering command argument types...");
		Registry.register(
			Registries.COMMAND_ARGUMENT_TYPE,
			BigGlobeMod.modID("enum"),
			(ArgumentSerializer)(new EnumArgumentSerializer())
		);
		BigGlobeMod.LOGGER.debug("Done registering command argument types.");
	}
}