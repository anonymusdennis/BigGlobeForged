package builderb0y.bigglobe.networking.base;

import net.minecraft.network.PacketByteBuf;

/**
 * Interface for sending packets back as a response.
 * This replaces the Fabric API's PacketSender interface.
 */
public interface PacketSender {
	void sendPacket(PacketByteBuf buffer);
}
