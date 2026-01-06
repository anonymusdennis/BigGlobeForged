package builderb0y.bigglobe.networking.base;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import net.minecraft.network.PacketByteBuf;

public interface S2CPlayPacketHandler<T> extends PacketHandler {

	@OnlyIn(Dist.CLIENT)
	public abstract T decode(
		PacketByteBuf buffer
	);


	@OnlyIn(Dist.CLIENT)
	public abstract void process(
		T data,
		PacketSender responseSender
	);
}