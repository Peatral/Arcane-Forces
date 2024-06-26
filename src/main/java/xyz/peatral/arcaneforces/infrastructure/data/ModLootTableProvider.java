package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import xyz.peatral.arcaneforces.ModBlocks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ), lookupProvider);
    }

    public static class BlockLoot extends BlockLootSubProvider {
        public BlockLoot(HolderLookup.Provider lookupProvider) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value).toList();
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.OLIBANUM_LOG.block().get());
            dropSelf(ModBlocks.OLIBANUM_WOOD.block().get());
            dropSelf(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get());
            dropSelf(ModBlocks.STRIPPED_OLIBANUM_WOOD.block().get());
            dropSelf(ModBlocks.OLIBANUM_SAPLING.block().get());
            add(ModBlocks.OLIBANUM_LEAVES.block().get(), block -> createOakLeavesDrops(block, ModBlocks.OLIBANUM_SAPLING.block().get(), NORMAL_LEAVES_SAPLING_CHANCES));

            dropSelf(ModBlocks.MYRRH_LOG.block().get());
            dropSelf(ModBlocks.MYRRH_WOOD.block().get());
            dropSelf(ModBlocks.STRIPPED_MYRRH_LOG.block().get());
            dropSelf(ModBlocks.STRIPPED_MYRRH_WOOD.block().get());
            dropSelf(ModBlocks.MYRRH_SAPLING.block().get());
            add(ModBlocks.MYRRH_LEAVES.block().get(), block -> createOakLeavesDrops(block, ModBlocks.MYRRH_SAPLING.block().get(), NORMAL_LEAVES_SAPLING_CHANCES));
        }
    }
}
