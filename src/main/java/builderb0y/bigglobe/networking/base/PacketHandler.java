package builderb0y.bigglobe.networking.base;

import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;

public interface PacketHandler {

	public default byte getId() {
		return BigGlobeNetwork.INSTANCE.getId(this);
	}

	public default PacketByteBuf buffer() {
		PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		buffer.writeByte(this.getId());
		return buffer;
	}
}

/**
 * Interface for sending packets back as a response
 */
interface PacketSender {
	void sendPacket(PacketByteBuf buffer);
}