package builderb0y.bigglobe.commands;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.mojang.brigadier.CommandDispatcher;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;

import builderb0y.bigglobe.BigGlobeMod;

public class BigGlobeCommands {

	public static final String NOT_APPLICABLE = "N/A";
	public static final DecimalFormat DECIMAL_FORMAT;
	static {
		DECIMAL_FORMAT = new DecimalFormat();
		DECIMAL_FORMAT.setDecimalSeparatorAlwaysShown(true);
		DECIMAL_FORMAT.setMinimumFractionDigits(1);
		DECIMAL_FORMAT.setMaximumFractionDigits(3);
		DecimalFormatSymbols symbols = DECIMAL_FORMAT.getDecimalFormatSymbols();
		symbols.setNaN(NOT_APPLICABLE);
		DECIMAL_FORMAT.setDecimalFormatSymbols(symbols);
	}

	public static String format(double number) {
		synchronized (DECIMAL_FORMAT) {
			return DECIMAL_FORMAT.format(number);
		}
	}

	public static void init() {
		BigGlobeMod.LOGGER.debug("Registering command event handler...");
		NeoForge.EVENT_BUS.addListener(BigGlobeCommands::onRegisterCommands);
		BigGlobeMod.LOGGER.debug("Done registering command event handler.");
	}

	public static void onRegisterCommands(RegisterCommandsEvent event) {
		registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
	}

	public static void registerCommands(
		CommandDispatcher<ServerCommandSource> dispatcher,
		CommandRegistryAccess registryAccess,
		RegistrationEnvironment environment
	) {
		BigGlobeMod.LOGGER.debug("Registering commands to dispatcher...");
		LocateCommand        .register(dispatcher);
		RespawnCommand       .register(dispatcher);
		EvaluateCommand      .register(dispatcher);
		DumpRegistriesCommand.register(dispatcher);
		DevDebugCommand      .register(dispatcher);
		TracyCommand         .register(dispatcher);
		BigGlobeMod.LOGGER.debug("Done registering commands to dispatcher.");
	}

	@OnlyIn(Dist.CLIENT)
	public static void initClient() {
		BigGlobeMod.LOGGER.debug("Registering client command event handler...");
		NeoForge.EVENT_BUS.addListener(BigGlobeCommands::onRegisterClientCommands);
		BigGlobeMod.LOGGER.debug("Done registering client command event handler.");
	}

	@OnlyIn(Dist.CLIENT)
	public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
		BigGlobeMod.LOGGER.debug("Registering client commands to dispatcher...");
		DisplayColumnsClientCommand.register(event.getDispatcher());
		SearchF3ClientCommand.register(event.getDispatcher());
		BigGlobeMod.LOGGER.debug("Done registering client commands to dispatcher.");
	}
}