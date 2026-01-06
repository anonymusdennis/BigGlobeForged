package builderb0y.bigglobe.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import net.minecraft.server.MinecraftServer;

public class ServerValue<T> implements Consumer<ServerStoppedEvent>, Supplier<T> {

	public final Supplier<T> supplier;
	public T value;

	public ServerValue(Supplier<T> supplier) {
		this.supplier = supplier;
		NeoForge.EVENT_BUS.addListener(this);
	}

	@Override
	public T get() {
		T value = this.value;
		if (value == null) {
			value = this.value = this.supplier.get();
		}
		return value;
	}

	@Override
	public void accept(ServerStoppedEvent event) {
		this.value = null;
	}
}