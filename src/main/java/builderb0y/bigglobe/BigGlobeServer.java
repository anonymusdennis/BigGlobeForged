package builderb0y.bigglobe;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import builderb0y.bigglobe.scripting.ServerPrintSink;
import builderb0y.scripting.environments.BuiltinScriptEnvironment;

@OnlyIn(Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = BigGlobeMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class BigGlobeServer {

	@SubscribeEvent
	public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
		BuiltinScriptEnvironment.PRINTER = new ServerPrintSink();
	}
}