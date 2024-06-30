package xyz.peatral.arcaneforces.infrastructure.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.Items.SPELL_HOLDERS)
                .add(
                        ModItems.CURSED_RING.get(),
                        ModItems.VOODOO_DOLL.get()
                );
        tag(ItemTags.SAPLINGS)
                .add(
                        ModBlocks.OLIBANUM_SAPLING.item().get(),
                        ModBlocks.MYRRH_SAPLING.item().get()
                );
        tag(ModTags.Items.OLIBANUM_LOGS).add(
                ModBlocks.OLIBANUM_LOG.item().get(),
                ModBlocks.OLIBANUM_WOOD.item().get(),
                ModBlocks.STRIPPED_OLIBANUM_LOG.item().get(),
                ModBlocks.STRIPPED_OLIBANUM_WOOD.item().get()
        );
        tag(ModTags.Items.MYRRH_LOGS).add(
                ModBlocks.MYRRH_LOG.item().get(),
                ModBlocks.MYRRH_WOOD.item().get(),
                ModBlocks.STRIPPED_MYRRH_LOG.item().get(),
                ModBlocks.STRIPPED_MYRRH_WOOD.item().get()
        );
        tag(ModTags.Items.INCENSE_LOGS)
                .addTag(ModTags.Items.OLIBANUM_LOGS)
                .addTag(ModTags.Items.MYRRH_LOGS);
        tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.INCENSE_LOGS);
        tag(ItemTags.LEAVES)
                .add(
                        ModBlocks.OLIBANUM_LEAVES.item().get(),
                        ModBlocks.MYRRH_LEAVES.item().get()
                );
        tag(ItemTags.CANDLES)
                .add(
                        ModBlocks.INCENSE_STICK.item().get()
                );
    }
}
