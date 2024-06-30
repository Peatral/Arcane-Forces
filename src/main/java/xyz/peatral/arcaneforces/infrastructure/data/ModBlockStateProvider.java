package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.content.incense.IncenseLogBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        incenselogBlockWithItem(ModBlocks.OLIBANUM_LOG.block().get());
        incenselogBlockWithItem(ModBlocks.MYRRH_LOG.block().get());
        logBlockWithItem(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get());
        logBlockWithItem(ModBlocks.STRIPPED_MYRRH_LOG.block().get());
        woodBlockWithItem(ModBlocks.OLIBANUM_WOOD.block().get(), ModBlocks.OLIBANUM_LOG.block().get());
        woodBlockWithItem(ModBlocks.MYRRH_WOOD.block().get(), ModBlocks.MYRRH_LOG.block().get());
        woodBlockWithItem(ModBlocks.STRIPPED_OLIBANUM_WOOD.block().get(), ModBlocks.STRIPPED_OLIBANUM_LOG.block().get());
        woodBlockWithItem(ModBlocks.STRIPPED_MYRRH_WOOD.block().get(), ModBlocks.STRIPPED_MYRRH_LOG.block().get());

        crossWithItem(ModBlocks.OLIBANUM_SAPLING.block().get());
        crossWithItem(ModBlocks.MYRRH_SAPLING.block().get());

        leavesWithItem(ModBlocks.OLIBANUM_LEAVES.block().get());
        leavesWithItem(ModBlocks.MYRRH_LEAVES.block().get());

        sticksWithItem(ModBlocks.INCENSE_STICK.block().get());
    }


    public void incenselogBlockWithItem(IncenseLogBlock block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (IncenseLogBlock.State state : IncenseLogBlock.State.values()) {
            String suffix = state == IncenseLogBlock.State.DEFAULT ? "" : "_" + state.getSerializedName();
            ResourceLocation side = blockTexture(block).withSuffix(suffix);
            ResourceLocation end = blockTexture(block).withSuffix("_top");
            BlockModelBuilder vertical = models().cubeColumn(name + suffix, side, end);
            BlockModelBuilder horizontal = models().cubeColumnHorizontal(name + "_horizontal" + suffix, side, end);
            builder.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(vertical).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(horizontal).rotationX(90).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).with(IncenseLogBlock.STATE, state)
                    .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        }
        simpleBlockItem(block);
    }

    public void sticksWithItem(Block block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

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
                                models().getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_" + number + "_" + candle + suffix)
                                        .parent(new ModelFile.UncheckedModelFile(parent))
                                        .texture("all", blockTexture(block).withSuffix(suffix))
                                        .texture("particle", blockTexture(block).withSuffix(suffix))
                        ).addModel();
            }
        }

        itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", itemTexture(block));
    }

    public void leavesWithItem(Block block) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(leaves(block)));
        simpleBlockItem(block);
    }

    public void crossWithItem(Block block) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(cross(block)));
        itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", blockTexture(block).getPath());
    }

    public ModelFile leaves(Block block) {
        return models().leaves(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block));
    }

    public ModelFile cross(Block block) {
        return models().cross(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block)).renderType("cutout");
    }

    public void logBlockWithItem(RotatedPillarBlock log) {
        logBlock(log);
        simpleBlockItem(log);
    }

    public void woodBlockWithItem(RotatedPillarBlock wood, RotatedPillarBlock log) {
        axisBlock(wood, blockTexture(log), blockTexture(log));
        simpleBlockItem(wood);
    }

    public void simpleBlockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(blockTexture(block).toString()));
    }

    public ResourceLocation itemTexture(Block block) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
    }
}
