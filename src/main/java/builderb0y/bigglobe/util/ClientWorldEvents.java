package builderb0y.bigglobe.util;

import java.util.ArrayList;
import java.util.List;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import net.minecraft.client.world.ClientWorld;

@OnlyIn(Dist.CLIENT)
public class ClientWorldEvents {

	private static final List<WorldChanged> LISTENERS = new ArrayList<>();

	public static final Event<WorldChanged> WORLD_CHANGED = new Event<WorldChanged>() {
		@Override
		public void register(WorldChanged listener) {
			LISTENERS.add(listener);
		}

		@Override
		public void invoke(ClientWorld oldWorld, ClientWorld newWorld) {
			for (WorldChanged listener : LISTENERS) {
				listener.worldChanged(oldWorld, newWorld);
			}
		}
	};

	@OnlyIn(Dist.CLIENT)
	public static interface WorldChanged {
		public abstract void worldChanged(ClientWorld oldWorld, ClientWorld newWorld);
	}

	@OnlyIn(Dist.CLIENT)
	public static interface Event<T extends WorldChanged> {
		void register(T listener);
		void invoke(ClientWorld oldWorld, ClientWorld newWorld);
	}
}