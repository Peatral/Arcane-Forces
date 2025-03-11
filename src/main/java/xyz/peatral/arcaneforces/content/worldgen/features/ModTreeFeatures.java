package xyz.peatral.arcaneforces.content.worldgen.features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModBlocks;

public class ModTreeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OLIBANUM = key("olibanum");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MYRRH = key("myrrh");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> pContext) {
        FeatureUtils.register(
                pContext,
                OLIBANUM,
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.OLIBANUM_LOG.get()),
                        new BendingTrunkPlacer(2, 1, 1, 2, ConstantInt.of(1)),
                        BlockStateProvider.simple(ModBlocks.OLIBANUM_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 1),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                .ignoreVines()
                .dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT))
                .build()
        );

        FeatureUtils.register(
                pContext,
                MYRRH,
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.MYRRH_LOG.get()),
                        new StraightTrunkPlacer(2, 1, 0),
                        BlockStateProvider.simple(ModBlocks.MYRRH_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                .ignoreVines()
                .dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT))
                .build());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
    }
}
