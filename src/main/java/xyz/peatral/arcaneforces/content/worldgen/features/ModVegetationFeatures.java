package xyz.peatral.arcaneforces.content.worldgen.features;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.content.worldgen.placements.ModTreePlacements;

import java.util.List;

public class ModVegetationFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FRAGRANT_DESERT = key("trees_fragrant_desert");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> holdergetter = context.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> olibanumHolder = holdergetter.getOrThrow(ModTreePlacements.OLIBANUM);
        Holder<PlacedFeature> myrrhHolder = holdergetter.getOrThrow(ModTreePlacements.MYRRH);

        FeatureUtils.register(
                context,
                TREES_FRAGRANT_DESERT,
                Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(
                        List.of(new WeightedPlacedFeature(olibanumHolder, 0.3F)),
                        myrrhHolder
                )
        );
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
    }
}
