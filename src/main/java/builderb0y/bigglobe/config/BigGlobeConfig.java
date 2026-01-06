package builderb0y.bigglobe.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import org.lwjgl.opengl.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

import builderb0y.autocodec.annotations.*;
import builderb0y.bigglobe.BigGlobeMod;
import builderb0y.bigglobe.columns.scripted.ScriptedColumn.UndergroundMode;
import builderb0y.bigglobe.compat.InstalledMods;
import builderb0y.bigglobe.mixinInterfaces.LodSystemHolder;
import builderb0y.bigglobe.rendering.lods.*;

//reminder: any time I add something new to this file, I need to add a lang entry for it too.
@UseFixer(name = "INSTANCE", in = BigGlobeConfigFixer.class, usage = MemberUsage.FIELD_CONTAINS_HANDLER)
public class BigGlobeConfig {

	public static final Supplier<BigGlobeConfig> INSTANCE = BigGlobeConfigLoader::loadConfig;
	public static void init() {}

	public void validatePostLoad() {
		this.threads = Math.max(Math.min(this.threads, Runtime.getRuntime().availableProcessors()), 1);
		this.moltenRockOreificationChance = MathHelper.clamp(this.moltenRockOreificationChance, 0.0F, 1.0F);
		this.lodRendering.validatePostLoad();
		this.distantHorizonsIntegration.validatePostLoad();
		this.voxyIntegration.validatePostLoad();
		this.playerSpawning.validatePostLoad();
	}

	
	@UseName("Default World Type")
	@DefaultIgnore
	public String defaultWorldType = "bigglobe:bigglobe";

	
	@UseName("Sanity Check World Height")
	@DefaultIgnore
	public boolean checkWorldHeight = true;

	
	@UseName("Big Globe Trees In Big Globe Worlds")
	@DefaultIgnore
	public boolean bigGlobeTreesInBigGlobeWorlds = true;

	
	@UseName("Hyperspace Enabled")
	@DefaultIgnore
	public boolean hyperspaceEnabled = true;

	
	@UseName("Molten Rock Ore-ification Chance")
	@DefaultIgnore
	public float moltenRockOreificationChance = 1.0F;

	
	@UseName("Threads")
	@DefaultIgnore
	public int threads = Math.max(Runtime.getRuntime().availableProcessors() - 4, 1);

	public int threads() {
		return Math.max(Math.min(this.threads, Runtime.getRuntime().availableProcessors()), 1);
	}

	
	@UseName("Player Spawning")
	
	@DefaultIgnore
	public final PlayerSpawning playerSpawning = new PlayerSpawning();

	public static class PlayerSpawning {

		
		@UseName("Max Spawn Radius")
		@DefaultIgnore
		public double maxSpawnRadius = 10000.0D;

		
		@UseName("Per-Player Spawn Points")
		@DefaultIgnore
		public boolean perPlayerSpawnPoints = false;

		public void validatePostLoad() {
			this.maxSpawnRadius = Math.max(this.maxSpawnRadius, 0.0D);
		}
	}

	
	@UseName("Data Pack Debugging")
	
	@DefaultIgnore
	public final DataPackDebugging dataPackDebugging = new DataPackDebugging();

	public static class DataPackDebugging {

		
		@UseName("Generate dependency graphs")
		@DefaultIgnore
		public boolean dependencyGraphs = false;

		
		@UseName("Print decision trees")
		@DefaultIgnore
		public boolean decisionTrees = false;

		
		@UseName("Log structure spawn attempts")
		@DefaultIgnore
		public boolean structureSpawning = false;

		
		@UseName("Log empty tags")
		@DefaultIgnore
		public boolean emptyTags = false;

		
		@UseName("Reject unused overriders")
		@DefaultIgnore
		public boolean rejectUnusedOverriders = false;

		
		@UseName("Invalid tag handling")
		
		@DefaultIgnore
		public InvalidTagHandling invalidTagHandling = InvalidTagHandling.VANILLA;

		
		@UseName("Log extra mob spawns")
		@DefaultIgnore
		public boolean logExtraMobSpawns = false;
	}

	public static enum InvalidTagHandling {
		VANILLA,
		FORCE_LOAD,
		FORCE_ABORT;
	}

	
	@UseName("LOD Rendering")
	
	@DefaultIgnore
	public final LodRendering lodRendering = new LodRendering();

	public static class LodRendering {

		public static enum EnabledMode {
			AUTO,
			ON,
			OFF;

			public boolean isEnabled() {
				return switch (this) {
					case AUTO -> !InstalledMods.DISTANT_HORIZONS && !InstalledMods.VOXY;
					case ON   -> true;
					case OFF  -> false;
				};
			}
		}

		
		@UseName("Enabled")
		
		@DefaultIgnore
		public EnabledMode enabled = EnabledMode.AUTO;
		
		public static transient boolean previousEnabled = EnabledMode.AUTO.isEnabled();

		public boolean renderingEnabled() {
			return this.enabled.isEnabled();
		}

		public static enum RendererBackend {
			AUTO,
			SIMPLE_SEPARATE,
			SIDED_SEPARATE,
			SIDED_COMBINED;

			@OnlyIn(Dist.CLIENT)
			public LodRenderer createRenderer(LodRendering config) {
				int quads = config.maxQuads;
				return switch (this) {
					case            AUTO -> GL.getCapabilities().GL_ARB_shader_draw_parameters ? new SidedCombinedLodRenderer(quads) : new SidedSeparateLodRenderer(quads);
					case SIMPLE_SEPARATE -> new SimpleLodRenderer(quads);
					case  SIDED_SEPARATE -> new SidedSeparateLodRenderer(quads);
					case  SIDED_COMBINED -> new SidedCombinedLodRenderer(quads);
				};
			}
		}

		
		@UseName("Renderer Backend")
		
		@DefaultIgnore
		public RendererBackend rendererBackend = RendererBackend.AUTO;
		
		public static transient RendererBackend previousRendererBackend = RendererBackend.AUTO;

		@OnlyIn(Dist.CLIENT)
		public LodRenderer createRendererBackend() {
			return this.rendererBackend.createRenderer(this);
		}

		
		@UseName("Maximum Quad Count")
		@DefaultIgnore
		
		@VerifyIntRange(min = 10_000_000L, max = 100_000_000L)
		public int maxQuads = 50_000_000;
		
		public static transient int previousMaxQuads = 50_000_000;

		
		@UseName("Quality")
		@DefaultIgnore
		public double quality = 2.0D;

		
		@UseName("Max LOD For Chunk Loading")
		@DefaultIgnore
		public int maxLodForChunkLoading = 5;
		
		public static transient int previousMaxLodForChunkLoading = 5;

		
		@UseName("Vertical Compression")
		@DefaultIgnore
		public int verticalCompression = 16;
		
		public static transient int previousVerticalCompression = 16;

		
		@UseName("Cave Culling Depth")
		@DefaultIgnore
		public int caveCullingDepth = 16;
		
		public static transient int previousCaveCullingDepth = 16;

		
		@UseName("Min View Distance")
		@DefaultIgnore
		public float minViewDistance = 0.25F;

		
		@UseName("Max View Distance")
		@DefaultIgnore
		public float maxViewDistance = 1024.0F;

		
		@UseName("Generation Buffer Distance")
		@DefaultIgnore
		public float generationBufferDistance = 1536.0F;

		
		@UseName("Fog Density")
		@DefaultIgnore
		public float fogDensity = 64.0F;

		
		@UseName("Fog Height Scale")
		@DefaultIgnore
		public float fogHeightScale = 4.0F;

		
		@UseName("Underground Mode")
		
		@DefaultIgnore
		public UndergroundMode undergroundMode = UndergroundMode.FILL;
		
		public static transient UndergroundMode previousUndergroundMode = UndergroundMode.FILL;

		public void validatePostLoad() {
			this.maxQuads = MathHelper.clamp(this.maxQuads, 10_000_000, 100_000_000);
			this.quality = MathHelper.clamp(this.quality, 1.0D, 3.0D);
			this.maxLodForChunkLoading = MathHelper.clamp(this.maxLodForChunkLoading, 0, 5);
			this.verticalCompression = Math.max(this.verticalCompression, 0);
			this.caveCullingDepth = Math.max(this.caveCullingDepth, -1);
			this.minViewDistance = Math.max(this.minViewDistance, 1.0F / 256.0F);
			this.maxViewDistance = Math.max(this.maxViewDistance, this.minViewDistance + 1.0F / 256.0F);
			this.generationBufferDistance = Math.max(this.generationBufferDistance, this.maxViewDistance);
			this.fogDensity = Math.max(this.fogDensity, 0.0F);
			this.fogHeightScale = Math.max(this.fogHeightScale, 0.0F);
			if (FMLEnvironment.dist == Dist.CLIENT) {
				this.maybeReloadLODs();
			}
		}

		@OnlyIn(Dist.CLIENT)
		public void maybeReloadLODs() {
			MinecraftClient client = MinecraftClient.getInstance();
			LodSystemHolder holder = client != null ? LodSystemHolder.of(client.worldRenderer) : null;
			if (
				this.renderingEnabled()    != previousEnabled               ||
				this.rendererBackend       != previousRendererBackend       ||
				this.maxQuads              != previousMaxQuads              ||
				this.undergroundMode       != previousUndergroundMode       ||
				this.maxLodForChunkLoading != previousMaxLodForChunkLoading ||
				this.verticalCompression   != previousVerticalCompression   ||
				this.caveCullingDepth      != previousCaveCullingDepth
			) {
				previousEnabled               = this.renderingEnabled();
				previousRendererBackend       = this.rendererBackend;
				previousMaxQuads              = this.maxQuads;
				previousUndergroundMode       = this.undergroundMode;
				previousMaxLodForChunkLoading = this.maxLodForChunkLoading;
				previousVerticalCompression   = this.verticalCompression;
				previousCaveCullingDepth      = this.caveCullingDepth;
				if (holder != null) LodSystem.reload(holder, client.world);
			}
			else if (holder != null) {
				LodSystem system = holder.bigglobe_getLodSystem();
				if (system != null) {
					system.qualityLimit = this.quality;
				}
			}
		}
	}

	
	@UseName("Distant Horizons Integration")
	
	@DefaultIgnore
	public final DistantHorizonsIntegration distantHorizonsIntegration = new DistantHorizonsIntegration();

	public static class DistantHorizonsIntegration {

		
		@UseName("Hyperspeed Generation")
		@DefaultIgnore
		public boolean hyperspeedGeneration = false;

		
		@UseName("Underground Mode")
		
		@DefaultIgnore
		public UndergroundMode undergroundMode = UndergroundMode.FILL;

		public void validatePostLoad() {}
	}

	
	@UseName("Voxy Integration")
	
	@DefaultIgnore
	public final VoxyIntegration voxyIntegration = new VoxyIntegration();

	public static class VoxyIntegration {

		
		@UseName("Use Worldgen Thread")
		@DefaultIgnore
		public boolean useWorldgenThread = true;

		
		@UseName("Underground Mode")
		
		@DefaultIgnore
		public UndergroundMode undergroundMode = UndergroundMode.NONE;

		
		@UseName("Light Air")
		@DefaultIgnore
		public boolean lightAir = false;

		public void validatePostLoad() {}
	}

	
	@UseName("C2ME Integration")
	
	@DefaultIgnore
	public final C2MEIntegration c2meIntegration = new C2MEIntegration();

	public static class C2MEIntegration {

		
		@UseName("Multi-Threaded Structures")
		@DefaultIgnore
		public boolean multiThreadedStructures = true;

		public boolean multiThreadedStructures() {
			return InstalledMods.C2ME && this.multiThreadedStructures;
		}
	}

	/**
	tricks AutoCodec into ignoring missing data and leaving
	the java object as it was after initialization,
	while simultaneously tricking intellij into
	*not* complaining that the object can be null.
	*/
	@VerifyNullable
	@Mirror(VerifyNullable.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	@SuppressWarnings("NullableProblems")
	public static @interface DefaultIgnore {}

	
	
	@UseName("Config Version")
	public static final int CONFIG_VERSION = 1;
}