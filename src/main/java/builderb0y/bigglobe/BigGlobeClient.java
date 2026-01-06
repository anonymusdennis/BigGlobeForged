package builderb0y.bigglobe;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import builderb0y.bigglobe.blocks.BigGlobeBlocks;
import builderb0y.bigglobe.commands.BigGlobeCommands;
import builderb0y.bigglobe.entities.BigGlobeEntityRenderers;
import builderb0y.bigglobe.fluids.BigGlobeFluids;
import builderb0y.bigglobe.hyperspace.HyperspaceDimensionEffects;
import builderb0y.bigglobe.items.BigGlobeItems;
import builderb0y.bigglobe.networking.base.BigGlobeNetwork;
import builderb0y.bigglobe.particles.BigGlobeParticles;
import builderb0y.bigglobe.rendering.lods.LodSystem;
import builderb0y.bigglobe.rendering.waypoints.WaypointWarpRenderer;
import builderb0y.bigglobe.scripting.ClientPrintSink;
import builderb0y.scripting.environments.BuiltinScriptEnvironment;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = BigGlobeMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BigGlobeClient {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		BigGlobeMod.LOGGER.info("Initializing client...");
		BigGlobeFluids.initClient();
		BigGlobeBlocks.initClient();
		BigGlobeItems.initClient();
		BigGlobeEntityRenderers.init();
		BigGlobeNetwork.initClient();
		BigGlobeCommands.initClient();
		BigGlobeParticles.initClient();
		HyperspaceDimensionEffects.init();
		LodSystem.init();
		#if MC_VERSION >= MC_1_21_9
			builderb0y.bigglobe.f3.BigGlobeDebugHudEntries.init();
		#endif
		BuiltinScriptEnvironment.PRINTER = new ClientPrintSink();
		BigGlobeMod.LOGGER.info("Done initializing client.");
	}
}