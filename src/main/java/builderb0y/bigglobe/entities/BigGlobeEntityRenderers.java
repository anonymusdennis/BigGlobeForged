package builderb0y.bigglobe.entities;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

import builderb0y.bigglobe.BigGlobeMod;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = BigGlobeMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BigGlobeEntityRenderers {

	public static void init() {
		BigGlobeMod.LOGGER.debug("Entity renderers will be registered via event...");
	}

	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		BigGlobeMod.LOGGER.debug("Registering entity renderers...");
		event.registerEntityRenderer(BigGlobeEntityTypes.TORCH_ARROW,  TorchArrowRenderer::new);
		event.registerEntityRenderer(BigGlobeEntityTypes.ROCK,   FlyingItemEntityRenderer::new);
		event.registerEntityRenderer(BigGlobeEntityTypes.STRING,     StringEntityRenderer::new);
		event.registerEntityRenderer(BigGlobeEntityTypes.WAYPOINT, WaypointEntityRenderer::new);
		BigGlobeMod.LOGGER.debug("Done registering entity renderers.");
	}
}