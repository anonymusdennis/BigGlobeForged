package builderb0y.bigglobe.fluids;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import builderb0y.bigglobe.BigGlobeMod;

public class BigGlobeFluids {

	public static final FlowableFluid
		SOUL_LAVA         = register("soul_lava",         new SoulLavaFluid.Still()),
		FLOWING_SOUL_LAVA = register("flowing_soul_lava", new SoulLavaFluid.Flowing());

	public static void init() {}

	@OnlyIn(Dist.CLIENT)
	public static void initClient() {
		// Fluid rendering in NeoForge is handled differently
		// The fluid type extensions handle the textures
	}

	public static <F extends Fluid> F register(String name, F fluid) {
		return Registry.register(Registries.FLUID, BigGlobeMod.modID(name), fluid);
	}
}