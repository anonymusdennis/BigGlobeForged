package builderb0y.bigglobe.rendering;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulLavaFogHandler {

	/**
	yes, it is kind of stupid to have an entire class just for this one field.
	I would've put it inside {@link net.minecraft.client.render.fog.LavaFogModifier} via mixin,
	but it would need to be public and static for that, which is not allowed.
	*/
	public static boolean inSoulLava;
}