package xyz.peatral.arcaneforces;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.incense.IncenseLogBlock;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Main.MOD_ID);


    public static final BlockWithItem<SaplingBlock, BlockItem> OLIBANUM_SAPLING = registerBlockAndItem("olibanum_sapling", () -> new SaplingBlock(
                    ModTrees.OLIBANUM,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY)),
            block -> new BlockItem(block, new Item.Properties())
    );

    public static final BlockWithItem<SaplingBlock, BlockItem> MYRRH_SAPLING = registerBlockAndItem("myrrh_sapling", () -> new SaplingBlock(
                    ModTrees.MYRRH,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY)),
            block -> new BlockItem(block, new Item.Properties())
    );

    public static final BlockWithItem<LeavesBlock, BlockItem> OLIBANUM_LEAVES = registerBlockAndItem(
            "olibanum_leaves",
            leaves(SoundType.GRASS),
            block -> new BlockItem(block, new Item.Properties())
    );
    public static final BlockWithItem<LeavesBlock, BlockItem> MYRRH_LEAVES = registerBlockAndItem(
            "myrrh_leaves",
            leaves(SoundType.GRASS),
            block -> new BlockItem(block, new Item.Properties())
    );


    public static final BlockWithItem<IncenseLogBlock, BlockItem> OLIBANUM_LOG = registerBlockAndItem("olibanum_log", IncenseLogBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> OLIBANUM_WOOD = registerBlockAndItem("olibanum_wood", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> STRIPPED_OLIBANUM_LOG = registerBlockAndItem("stripped_olibanum_log", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> STRIPPED_OLIBANUM_WOOD = registerBlockAndItem("stripped_olibanum_wood", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<IncenseLogBlock, BlockItem> MYRRH_LOG = registerBlockAndItem("myrrh_log", IncenseLogBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> MYRRH_WOOD = registerBlockAndItem("myrrh_wood", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> STRIPPED_MYRRH_LOG = registerBlockAndItem("stripped_myrrh_log", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));
    public static final BlockWithItem<RotatedPillarBlock, BlockItem> STRIPPED_MYRRH_WOOD = registerBlockAndItem("stripped_myrrh_wood", log(MapColor.WOOD, MapColor.PODZOL), block -> new BlockItem(block, new Item.Properties()));

    public static final BlockWithItem<CandleBlock, BlockItem> INCENSE_STICK = registerBlockAndItem("incense_stick",
            () -> new CandleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .noOcclusion()
                    .strength(0.1F)
                    .sound(SoundType.CANDLE)
                    .lightLevel(CandleBlock.LIGHT_EMISSION)
                    .pushReaction(PushReaction.DESTROY)
            ), block -> new BlockItem(block, new Item.Properties()));

    @SubscribeEvent
    public static void blockColors(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null
                        ? BiomeColors.getAverageFoliageColor(pLevel, pPos)
                        : FoliageColor.getDefaultColor(),
                OLIBANUM_LEAVES.block.get(),
                MYRRH_LEAVES.block.get()
        );
    }

    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> FoliageColor.getDefaultColor(),
                OLIBANUM_LEAVES.item.get(),
                MYRRH_LEAVES.item.get()
        );
    }

    public static BlockState getAxeStrippingState(BlockState originalState) {
        // TODO: This should be improved
        Map<Block, Block> STRIPPABLES = new ImmutableMap.Builder<Block, Block>()
                .put(OLIBANUM_WOOD.block.get(), STRIPPED_OLIBANUM_WOOD.block.get())
                .put(OLIBANUM_LOG.block.get(), STRIPPED_OLIBANUM_LOG.block.get())
                .put(MYRRH_WOOD.block.get(), STRIPPED_MYRRH_WOOD.block.get())
                .put(MYRRH_LOG.block.get(), STRIPPED_MYRRH_LOG.block.get())
                .build();

        Block block = STRIPPABLES.get(originalState.getBlock());
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockWithItem<BLOCK, ITEM> registerBlockAndItem(
            String name,
            Supplier<BLOCK> blockFactory,
            Function<? super BLOCK, ITEM> itemFactory) {
        DeferredHolder<Block, BLOCK> block = BLOCKS.register(name, blockFactory);
        DeferredHolder<Item, ITEM> item = ModItems.ITEMS.register(name, () -> itemFactory.apply(block.get()));
        return new BlockWithItem<>(block, item);
    }

    private static Supplier<RotatedPillarBlock> log(MapColor pTopMapColor, MapColor pSideMapColor) {
        return () -> new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? pTopMapColor : pSideMapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }

    private static Supplier<LeavesBlock> leaves(SoundType pSoundType) {
        return () -> new LeavesBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(pSoundType)
                        .noOcclusion()
                        .isSuffocating((pState, pLevel, pPos) -> false)
                        .isViewBlocking((pState, pLevel, pPos) -> false)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor((pState, pLevel, pPos) -> false)
        );
    }

    public record BlockWithItem<BLOCK extends Block, ITEM extends BlockItem>(DeferredHolder<Block, BLOCK> block,
                                                                             DeferredHolder<Item, ITEM> item) {
    }
}
