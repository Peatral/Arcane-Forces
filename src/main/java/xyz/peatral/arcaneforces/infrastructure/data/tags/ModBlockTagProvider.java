package xyz.peatral.arcaneforces.infrastructure.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.SAPLINGS)
                .add(
                        ModBlocks.OLIBANUM_SAPLING.get(),
                        ModBlocks.MYRRH_SAPLING.get()
                );
        tag(ModTags.Blocks.OLIBANUM_LOGS).add(
                ModBlocks.OLIBANUM_LOG.get(),
                ModBlocks.OLIBANUM_WOOD.get(),
                ModBlocks.STRIPPED_OLIBANUM_LOG.get(),
                ModBlocks.STRIPPED_OLIBANUM_WOOD.get()
        );
        tag(ModTags.Blocks.MYRRH_LOGS).add(
                ModBlocks.MYRRH_LOG.get(),
                ModBlocks.MYRRH_WOOD.get(),
                ModBlocks.STRIPPED_MYRRH_LOG.get(),
                ModBlocks.STRIPPED_MYRRH_WOOD.get()
        );
        tag(ModTags.Blocks.INCENSE_LOGS)
                .addTag(ModTags.Blocks.OLIBANUM_LOGS)
                .addTag(ModTags.Blocks.MYRRH_LOGS);
        tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.INCENSE_LOGS);
        tag(BlockTags.LEAVES)
                .add(
                        ModBlocks.OLIBANUM_LEAVES.get(),
                        ModBlocks.MYRRH_LEAVES.get()
                );
        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(
                        ModBlocks.OLIBANUM_LEAVES.get(),
                        ModBlocks.MYRRH_LEAVES.get()
                );
    }
}
