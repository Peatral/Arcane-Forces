package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.Optional;

public class ModTrees {
    public static final TreeGrower OLIBANUM = new TreeGrower(
            "olibanum",
            0.1f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(Features.OLIBANUM),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static final TreeGrower MYRRH = new TreeGrower(
            "myrrh",
            0.1f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(Features.MYRRH),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static class Features {
        public static final ResourceKey<ConfiguredFeature<?, ?>> OLIBANUM = key("olibanum");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MYRRH = key("myrrh");

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> pContext) {
            FeatureUtils.register(pContext, OLIBANUM, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.OLIBANUM_LOG.block().get()),
                    new BendingTrunkPlacer(2, 1, 1, 2, ConstantInt.of(1)),
                    BlockStateProvider.simple(ModBlocks.OLIBANUM_LEAVES.block().get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 1),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build());

            FeatureUtils.register(pContext, MYRRH, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.MYRRH_LOG.block().get()),
                    new StraightTrunkPlacer(2, 1, 0),
                    BlockStateProvider.simple(ModBlocks.MYRRH_LEAVES.block().get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build());
        }

        private static TreeConfiguration.TreeConfigurationBuilder createSmallTree(
                Block pLogBlock, Block pLeavesBlock, int pBaseHeight, int pRadius
        ) {
            return new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(pLogBlock),
                    new StraightTrunkPlacer(pBaseHeight, 0, 0),
                    BlockStateProvider.simple(pLeavesBlock),
                    new BlobFoliagePlacer(ConstantInt.of(pRadius), ConstantInt.of(0), 2),
                    new TwoLayersFeatureSize(1, 0, 1)
            );
        }

        private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
        }
    }

}
