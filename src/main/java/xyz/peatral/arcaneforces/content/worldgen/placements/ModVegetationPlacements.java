package xyz.peatral.arcaneforces.content.worldgen.placements;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.content.worldgen.features.ModVegetationFeatures;

public class ModVegetationPlacements {
    public static final ResourceKey<PlacedFeature> TREES_FRAGRANT_DESERT = key("trees_fragrant_desert");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> fragrantTreesHolder = holdergetter.getOrThrow(ModVegetationFeatures.TREES_FRAGRANT_DESERT);
        PlacementUtils.register(
                context,
                TREES_FRAGRANT_DESERT,
                fragrantTreesHolder,
                PlacementUtils.countExtra(0, 0.05F, 1),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
        );
    }

    private static ResourceKey<PlacedFeature> key(String pKey) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, pKey));
    }
}
