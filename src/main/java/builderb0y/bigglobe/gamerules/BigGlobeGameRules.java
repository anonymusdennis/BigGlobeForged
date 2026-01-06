package builderb0y.bigglobe.gamerules;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.IntRule;

import builderb0y.bigglobe.BigGlobeMod;
import builderb0y.bigglobe.networking.packets.DangerousRapidsPacket;
import builderb0y.bigglobe.networking.packets.TimeSpeedS2CPacketHandler;

public class BigGlobeGameRules {

	static { BigGlobeMod.LOGGER.debug("Registering game rules..."); }

	// NeoForge uses vanilla gamerule registration
	// Using IntRule for daylight cycle speed (multiplied by 100 for precision)
	public static final GameRules.Key<IntRule> DAYLIGHT_CYCLE_SPEED = GameRules.register(
		"bigglobe:daylightCycleSpeed",
		GameRules.Category.UPDATES,
		GameRules.IntRule.create(100, (MinecraftServer server, IntRule rule) -> {
			server.getPlayerManager().getPlayerList().forEach(
				TimeSpeedS2CPacketHandler.INSTANCE::send
			);
		})
	);

	public static final GameRules.Key<BooleanRule> SOUL_LAVA_SOURCE_CONVERSION = GameRules.register(
		"bigglobe:soulLavaSourceConversion",
		GameRules.Category.UPDATES,
		GameRules.BooleanRule.create(false)
	);

	public static final GameRules.Key<BooleanRule> DANGEROUS_RAPIDS = GameRules.register(
		"bigglobe:dangerousRapids",
		GameRules.Category.UPDATES,
		GameRules.BooleanRule.create(true, (MinecraftServer server, BooleanRule rule) -> {
			server.getPlayerManager().getPlayerList().forEach(
				DangerousRapidsPacket.INSTANCE::send
			);
		})
	);

	static { BigGlobeMod.LOGGER.debug("Done registering game rules."); }

	public static void init() {
		//trigger static initializer.
	}

	/** Helper to get daylight cycle speed as double (divide by 100) */
	public static double getDaylightCycleSpeed(GameRules rules) {
		return rules.getInt(DAYLIGHT_CYCLE_SPEED) / 100.0D;
	}
}