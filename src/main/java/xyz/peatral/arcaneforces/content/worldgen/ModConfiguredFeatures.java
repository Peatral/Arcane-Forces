package xyz.peatral.arcaneforces.content.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import xyz.peatral.arcaneforces.content.worldgen.features.ModTreeFeatures;
import xyz.peatral.arcaneforces.content.worldgen.features.ModVegetationFeatures;

public class ModConfiguredFeatures {
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ModTreeFeatures.bootstrap(context);
        ModVegetationFeatures.bootstrap(context);
    }
}
