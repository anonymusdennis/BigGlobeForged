package builderb0y.bigglobe.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.world.ClientWorld;

@OnlyIn(Dist.CLIENT)
public class ClientWorldEvents {

	public static final Event<WorldChanged> WORLD_CHANGED = EventFactory.createArrayBacked(WorldChanged.class, (WorldChanged[] events) -> {
		return (ClientWorld woldWorld, ClientWorld newWorld) -> {
			for (WorldChanged event : events) {
				event.worldChanged(woldWorld, newWorld);
			}
		};
	});

	@OnlyIn(Dist.CLIENT)
	public static interface WorldChanged {

		public abstract void worldChanged(ClientWorld oldWorld, ClientWorld newWorld);
	}
}