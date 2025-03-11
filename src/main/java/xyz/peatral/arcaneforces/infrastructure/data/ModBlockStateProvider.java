package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.*;
import xyz.peatral.arcaneforces.content.incense.IncenseLogBlock;

public class ModBlockStateProvider {

    public static void incenselogBlockWithItem(BlockStateProvider blockStateProvider, ResourceLocation location, IncenseLogBlock block) {
        VariantBlockStateBuilder builder = blockStateProvider.getVariantBuilder(block);
        for (IncenseLogBlock.State state : IncenseLogBlock.State.values()) {
            String suffix = state == IncenseLogBlock.State.DEFAULT ? "" : "_" + state.getSerializedName();
            ResourceLocation side = blockStateProvider.blockTexture(block).withSuffix(suffix);
            ResourceLocation end = blockStateProvider.blockTexture(block).withSuffix("_top");
            BlockModelBuilder vertical = blockStateProvider.models().cubeColumn(location.getPath() + suffix, side, end);
            BlockModelBuilder horizontal = blockStateProvider.models().cubeColumnHorizontal(location.getPath() + "_horizontal" + suffix, side, end);
            builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(vertical).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(horizontal).rotationX(90).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        }
    }

    public static void sticksWithItem(BlockStateProvider blockStateProvider, Block block, ResourceLocation location) {
        VariantBlockStateBuilder builder = blockStateProvider.getVariantBuilder(block);

        for (int i = 1; i <= 4; i++) {
            String number = new String[]{"one", "two", "three", "four"}[i - 1];
            String candle = i > 1 ? "candles" : "candle";
            String parent = i == 1 ? "block/template_candle" : "block/template_" + number + "_" + candle;
            for (boolean lit : new boolean[]{true, false}) {
                String suffix = lit ? "_lit" : "";
                builder.partialState()
                        .with(CandleBlock.CANDLES, i)
                        .with(CandleBlock.LIT, lit)
                        .modelForState().modelFile(
                                blockStateProvider.models().getBuilder(location.getPath() + "_" + number + "_" + candle + suffix)
                                        .parent(new ModelFile.UncheckedModelFile(parent))
                                        .texture("all", blockStateProvider.blockTexture(block).withSuffix(suffix))
                                        .texture("particle", blockStateProvider.blockTexture(block).withSuffix(suffix))
                        ).addModel();
            }
        }
    }

    public static void leavesWithItem(BlockStateProvider blockStateProvider, Block block) {
        blockStateProvider.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(leaves(blockStateProvider, block)));
    }

    public static void crossWithItem(BlockStateProvider blockStateProvider, Block block) {
        blockStateProvider.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(cross(blockStateProvider, block)));
        blockStateProvider.itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", blockStateProvider.blockTexture(block).getPath());
    }

    public static ModelFile leaves(BlockStateProvider blockStateProvider, Block block) {
        return blockStateProvider.models().leaves(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockStateProvider.blockTexture(block));
    }

    public static ModelFile cross(BlockStateProvider blockStateProvider, Block block) {
        return blockStateProvider.models().cross(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockStateProvider.blockTexture(block)).renderType("cutout");
    }

    public static void logBlockWithItem(BlockStateProvider blockStateProvider, RotatedPillarBlock log) {
        blockStateProvider.logBlock(log);
    }

    public static void woodBlockWithItem(BlockStateProvider blockStateProvider, RotatedPillarBlock wood, RotatedPillarBlock log) {
        blockStateProvider.axisBlock(wood, blockStateProvider.blockTexture(log), blockStateProvider.blockTexture(log));
    }
}
