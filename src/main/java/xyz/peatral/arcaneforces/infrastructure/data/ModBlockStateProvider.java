package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.peatral.arcaneforces.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlockWithItem(ModBlocks.OLIBANUM_LOG.block().get());
        logBlockWithItem(ModBlocks.MYRRH_LOG.block().get());
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
}
