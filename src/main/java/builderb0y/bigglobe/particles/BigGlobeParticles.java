package builderb0y.bigglobe.particles;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BigGlobeParticles {

	public static void init() {
		SporeParticles.init();
	}

	@OnlyIn(Dist.CLIENT)
	public static void initClient() {
		SporeParticles.initClient();
	}
}