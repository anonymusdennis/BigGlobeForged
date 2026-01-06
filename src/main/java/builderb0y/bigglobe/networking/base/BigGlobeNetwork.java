package builderb0y.bigglobe.networking.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import builderb0y.bigglobe.BigGlobeMod;
import builderb0y.bigglobe.networking.packets.*;
import builderb0y.bigglobe.versions.EntityVersions;

public class BigGlobeNetwork {

	public static final Identifier NETWORK_ID = BigGlobeMod.modID("network");
	public static final Logger LOGGER = LoggerFactory.getLogger(BigGlobeMod.MODNAME + "/Network");
	public static final BigGlobeNetwork INSTANCE = new BigGlobeNetwork();

	public final List<PacketHandler> idToHandler = new ArrayList<>(16);
	public final Object2ByteMap<PacketHandler> handlerToId = new Object2ByteOpenHashMap<>(2);

	public BigGlobeNetwork() {
		this.handlerToId.defaultReturnValue((byte)(-1));
		this.register(SettingsSyncS2CPacketHandler.INSTANCE);
		this.register(TimeSpeedS2CPacketHandler.INSTANCE);
		this.register(DangerousRapidsPacket.INSTANCE);
		this.register(WaypointListS2CPacket.INSTANCE);
		this.register(WaypointAddS2CPacket.INSTANCE);
		this.register(WaypointRemoveS2CPacket.INSTANCE);
		this.register(UseWaypointPacket.INSTANCE);
		this.register(WaypointRenameC2SPacket.INSTANCE);
		this.register(WaypointRemoveC2SPacket.INSTANCE);
	}

	public byte nextId() {
		int id = this.idToHandler.size();
		if (id < 255) return (byte)(id);
		else throw new IllegalStateException("Too many packet handlers registered on " + this);
	}

	public void register(PacketHandler handler) {
		byte id = this.nextId();
		this.idToHandler.add(handler);
		this.handlerToId.put(handler, id);
	}

	public byte getId(PacketHandler handler) {
		byte id = this.handlerToId.getByte(handler);
		if (id != -1) return id;
		else throw new IllegalStateException(handler + " not registered on " + this);
	}

	public @Nullable PacketHandler getHandler(byte id) {
		int unsignedId = Byte.toUnsignedInt(id);
		if (unsignedId < this.idToHandler.size()) return this.idToHandler.get(unsignedId);
		else return null;
	}

	public void handleClientPayload(BigGlobePayload payload, IPayloadContext context) {
		byte id = payload.buffer().readByte();
		if (this.getHandler(id) instanceof S2CPlayPacketHandler<?> packetHandler) {
			this.doReceiveOnClient(context, payload.buffer(), packetHandler);
		}
		else {
			LOGGER.warn("No server to client play packet handler registered for ID " + Byte.toUnsignedInt(id));
		}
	}

	public void handleServerPayload(BigGlobePayload payload, IPayloadContext context) {
		byte id = payload.buffer().readByte();
		if (this.getHandler(id) instanceof C2SPlayPacketHandler<?> packetHandler) {
			ServerPlayerEntity player = (ServerPlayerEntity) context.player();
			this.doReceiveOnServer(EntityVersions.getServer(player), player, payload.buffer(), context, packetHandler);
		}
		else {
			LOGGER.warn("No client to server play packet handler registered for ID " + Byte.toUnsignedInt(id));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public <T> void doReceiveOnClient(
		IPayloadContext context,
		PacketByteBuf buffer,
		S2CPlayPacketHandler<T> handler
	) {
		handler.process(handler.decode(buffer), new NeoForgePacketSender(context));
	}

	public <T> void doReceiveOnServer(
		MinecraftServer server,
		ServerPlayerEntity player,
		PacketByteBuf buffer,
		IPayloadContext context,
		C2SPlayPacketHandler<T> handler
	) {
		handler.process(player, handler.decode(player, buffer), new NeoForgePacketSender(context));
	}

	public void sendToPlayer(ServerPlayerEntity player, PacketByteBuf buffer) {
		PacketDistributor.sendToPlayer(player, new BigGlobePayload(buffer));
	}

	public void sendToServer(PacketByteBuf buffer) {
		PacketDistributor.sendToServer(new BigGlobePayload(buffer));
	}

	public static void init() {
		LOGGER.debug("Initializing common network...");
		// Network registration is handled by the event below
		LOGGER.debug("Done initializing common network.");
	}

	@OnlyIn(Dist.CLIENT)
	public static void initClient() {
		LOGGER.debug("Initializing client network...");
		// Client network registration is handled by the event
		LOGGER.debug("Done initializing client network.");
	}

	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(BigGlobeMod.MODID);
		registrar.playBidirectional(
			BigGlobePayload.ID,
			BigGlobePayload.CODEC,
			(payload, context) -> {
				if (context.flow().isClientbound()) {
					INSTANCE.handleClientPayload(payload, context);
				} else {
					INSTANCE.handleServerPayload(payload, context);
				}
			}
		);
	}

	/**
	 * Wrapper to adapt NeoForge's IPayloadContext to the PacketSender interface
	 */
	public static class NeoForgePacketSender implements PacketSender {
		private final IPayloadContext context;

		public NeoForgePacketSender(IPayloadContext context) {
			this.context = context;
		}

		@Override
		public void sendPacket(PacketByteBuf buffer) {
			this.context.reply(new BigGlobePayload(buffer));
		}
	}
}