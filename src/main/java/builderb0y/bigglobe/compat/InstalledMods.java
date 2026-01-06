package builderb0y.bigglobe.compat;

import net.neoforged.fml.ModList;

public class InstalledMods {

	public static final boolean
		C2ME              = ModList.get().isLoaded("c2me"),
		DISTANT_HORIZONS  = ModList.get().isLoaded("distanthorizons"),
		VOXY              = ModList.get().isLoaded("voxy");
	//if setup fails for either of these two mods,
	//we will pretend they are not installed for
	//the remainder of the time the game stays open.
	public static boolean
		DIMLIB            = ModList.get().isLoaded("dimlib"),
		IMMERSIVE_PORTALS = ModList.get().isLoaded("immersive_portals");
}