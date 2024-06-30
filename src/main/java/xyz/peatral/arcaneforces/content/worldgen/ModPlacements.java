package xyz.peatral.arcaneforces.content.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.peatral.arcaneforces.content.worldgen.placements.ModTreePlacements;
import xyz.peatral.arcaneforces.content.worldgen.placements.ModVegetationPlacements;

public class ModPlacements {
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        ModTreePlacements.bootstrap(context);
        ModVegetationPlacements.bootstrap(context);
    }
}
