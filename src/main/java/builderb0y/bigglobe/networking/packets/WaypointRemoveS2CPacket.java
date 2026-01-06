package builderb0y.bigglobe.networking.packets;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigglobe.hyperspace.PlayerWaypointManager;
import builderb0y.bigglobe.mixinInterfaces.WaypointTracker;
import builderb0y.bigglobe.networking.base.BigGlobeNetwork;
import builderb0y.bigglobe.networking.base.S2CPlayPacketHandler;

public class WaypointRemoveS2CPacket implements S2CPlayPacketHandler<Integer> {

	public static final WaypointRemoveS2CPacket INSTANCE = new WaypointRemoveS2CPacket();

	public void send(ServerPlayerEntity player, int id) {
		PacketByteBuf buffer = this.buffer();
		buffer.writeVarInt(id);
		BigGlobeNetwork.INSTANCE.sendToPlayer(player, buffer);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Integer decode(PacketByteBuf buffer) {
		return buffer.readVarInt();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void process(Integer data, PacketSender responseSender) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			PlayerWaypointManager manager = PlayerWaypointManager.get(player);
			if (manager != null) {
				manager.removeWaypoint(data, true);
			}
		}
	}
}