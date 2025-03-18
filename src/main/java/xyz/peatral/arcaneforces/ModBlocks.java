package xyz.peatral.arcaneforces;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Direction;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.ShrineAltarBlock;
import xyz.peatral.arcaneforces.content.shrines.waystone.WaystoneBlock;
import xyz.peatral.arcaneforces.content.incense.IncenseLogBlock;
import xyz.peatral.arcaneforces.content.worldgen.ModTrees;
import xyz.peatral.arcaneforces.infrastructure.data.ModBlockStateProvider;

import java.util.Map;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModBlocks {

    public static final BlockEntry<SaplingBlock> OLIBANUM_SAPLING = sapling("olibanum_sapling", ModTrees.OLIBANUM, "Olibanum Sapling");
    public static final BlockEntry<SaplingBlock> MYRRH_SAPLING = sapling("myrrh_sapling", ModTrees.MYRRH, "Myrrh Sapling");

    public static final BlockEntry<LeavesBlock> OLIBANUM_LEAVES = leaves("olibanum_leaves", OLIBANUM_SAPLING, "Olibanum Leaves");
    public static final BlockEntry<LeavesBlock> MYRRH_LEAVES = leaves("myrrh_leaves", MYRRH_SAPLING, "Myrrh Leaves");

    public static final BlockEntry<IncenseLogBlock> OLIBANUM_LOG = incenseLog("olibanum_log", "Olibanum Log");
    public static final BlockEntry<RotatedPillarBlock> OLIBANUM_WOOD = woodBlock("olibanum_wood", OLIBANUM_LOG,  "Olibanum Wood");
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_OLIBANUM_LOG = log("stripped_olibanum_log", "Stripped Olibanum Log");
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_OLIBANUM_WOOD = woodBlock("stripped_olibanum_wood", STRIPPED_OLIBANUM_LOG, "Stripped Olibanum Wood");

    public static final BlockEntry<IncenseLogBlock> MYRRH_LOG = incenseLog("myrrh_log","Myrrh Log");
    public static final BlockEntry<RotatedPillarBlock> MYRRH_WOOD = woodBlock("myrrh_wood", MYRRH_LOG, "Myrrh Wood");
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MYRRH_LOG = log("stripped_myrrh_log", "Stripped Myrrh Log");
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MYRRH_WOOD = woodBlock("stripped_myrrh_wood", STRIPPED_MYRRH_LOG, "Stripped Myrrh Wood");

    public static final BlockEntry<CandleBlock> INCENSE_STICK = REGISTRATE.get().block(
            "incense_stick",
            properties -> new CandleBlock(properties
                    .mapColor(MapColor.WOOD)
                    .noOcclusion()
                    .strength(0.1F)
                    .sound(SoundType.CANDLE)
                    .lightLevel(CandleBlock.LIGHT_EMISSION)
                    .pushReaction(PushReaction.DESTROY)
            ))
            .blockstate((context, provider) -> ModBlockStateProvider.sticksWithItem(provider, context.get(), context.getId().withPrefix("block/incense_stick/")))
            .lang("Incense Stick")
            .loot((lootTables, candleBlock) -> lootTables.add(candleBlock, lootTables.createCandleDrops(candleBlock)))
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<WaystoneBlock> WAYSTONE = REGISTRATE.get().block("waystone", WaystoneBlock::new)
            .blockstate((c, p) -> p.getVariantBuilder(c.get())
                    .partialState().with(WaystoneBlock.ACTIVATED, false)
                            .modelForState()
                            .modelFile(p.models().withExistingParent("block/waystone/not_activated", Main.asResource("block/waystone/base"))
                                    .texture("particle", Main.asResource("block/waystone/waystone_base"))
                                    .texture("base", Main.asResource("block/waystone/waystone_base"))
                                    .texture("pillar", Main.asResource("block/waystone/waystone_pillar"))
                            )
                            .addModel()
                    .partialState().with(WaystoneBlock.ACTIVATED, true)
                            .modelForState()
                            .modelFile(p.models().withExistingParent("block/waystone/activated", Main.asResource("block/waystone/base"))
                                    .texture("particle", Main.asResource("block/waystone/waystone_base"))
                                    .texture("base", Main.asResource("block/waystone/waystone_base"))
                                    .texture("pillar", Main.asResource("block/waystone/waystone_pillar_activated"))
                            )
                            .addModel()
            )
            .lang("Waystone")
            .defaultLoot()
            .item().model((c, p) -> p.withExistingParent("waystone", Main.asResource("block/waystone/not_activated"))).build()
            .register();

    public static final BlockEntry<ShrineAltarBlock> SHRINE_ALTAR = REGISTRATE.get().block("shrine_altar", ShrineAltarBlock::new)
            .blockstate((c, p) -> p.getVariantBuilder(c.get())
                    .partialState()
                    .modelForState()
                    .modelFile(p.models().withExistingParent("block/shrine_altar/not_activated", Main.asResource("block/shrine_altar/base"))
                            .texture("particle", Main.asResource("block/shrine_altar/shrine_altar_base"))
                            .texture("base", Main.asResource("block/shrine_altar/shrine_altar_base"))
                            .texture("pillar", Main.asResource("block/shrine_altar/shrine_altar_pillar"))
                            .texture("top", Main.asResource("block/shrine_altar/shrine_altar_top"))
                    )
                    .addModel()
            )
            .lang("Shrine Altar")
            .defaultLoot()
            .item().model((c, p) -> p.withExistingParent("shrine_altar", Main.asResource("block/shrine_altar/not_activated"))).build()
            .register();


    public static final NonNullFunction<BlockBehaviour.Properties, RotatedPillarBlock> logBlock() {
        return (properties) -> new RotatedPillarBlock(
                properties
                        .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.PODZOL)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }

    public static final <T extends Block> BlockEntry<T> block(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, String translation) {
        return REGISTRATE.get().block(name, factory).lang(translation).defaultLoot().item().build().register();
    }

    public static final <T extends Block> BlockEntry<T> block(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> state, String translation) {
        return REGISTRATE.get()
                .block(name, factory)
                .lang(translation)
                .blockstate(state)
                .defaultLoot()
                .item().build()
                .register();
    }

    public static final BlockEntry<SaplingBlock> sapling(String name, TreeGrower grower, String translation) {
        return block(
                name,
                properties -> new SaplingBlock(
                        grower,
                        properties
                                .mapColor(MapColor.PLANT)
                                .noCollission()
                                .randomTicks()
                                .instabreak()
                                .sound(SoundType.GRASS)
                                .pushReaction(PushReaction.DESTROY)
                ),
                (context, provider) -> ModBlockStateProvider.crossWithItem(provider, context.get()),
                translation
        );
    }

    public static final BlockEntry<IncenseLogBlock> incenseLog(String name, String translation) {
        return REGISTRATE.get()
                .block(name, IncenseLogBlock::new)
                .lang(translation)
                .blockstate((c, p) -> ModBlockStateProvider.incenselogBlockWithItem(p, c.getId().withPrefix("block/" + name + "/"), c.get()))
                .defaultLoot()
                .item().model((c, p) -> p.withExistingParent(name, c.getId().withPrefix("block/" + name + "/"))).build()
                .register();
    }

    public static final BlockEntry<RotatedPillarBlock> log(String name, String translation) {
        return block(name, logBlock(), (context, provider) -> ModBlockStateProvider.logBlockWithItem(provider, context.get()), translation);
    }

    public static final BlockEntry<RotatedPillarBlock> woodBlock(String name, NonNullSupplier<? extends RotatedPillarBlock> log, String translation) {
        return block(name, logBlock(), (context, provider) -> ModBlockStateProvider.woodBlockWithItem(provider, context.get(), log.get()), translation);
    }

    private static BlockEntry<LeavesBlock> leaves(String name, NonNullSupplier<SaplingBlock> saplingBlock, String translation) {
        return REGISTRATE.get()
                .block(name, properties -> new LeavesBlock(
                    properties
                            .mapColor(MapColor.PLANT)
                            .strength(0.2F)
                            .randomTicks()
                            .sound(SoundType.GRASS)
                            .noOcclusion()
                            .isSuffocating((pState, pLevel, pPos) -> false)
                            .isViewBlocking((pState, pLevel, pPos) -> false)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
                            .isRedstoneConductor((pState, pLevel, pPos) -> false)
                ))
                .blockstate((context, provider) -> ModBlockStateProvider.leavesWithItem(provider, context.get()))
                .lang(translation)
                .loot((lootTables, block) -> lootTables.add(block, lootTables.createLeavesDrops(block, saplingBlock.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F)))
                .item().build()
                .register();
    }

    @SubscribeEvent
    public static void blockColors(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null
                        ? BiomeColors.getAverageFoliageColor(pLevel, pPos)
                        : FoliageColor.getDefaultColor(),
                OLIBANUM_LEAVES.get(),
                MYRRH_LEAVES.get()
        );
    }

    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> FoliageColor.getDefaultColor(),
                OLIBANUM_LEAVES.get(),
                MYRRH_LEAVES.get()
        );
    }

    public static BlockState getAxeStrippingState(BlockState originalState) {
        // TODO: This should be improved
        Map<Block, Block> STRIPPABLES = new ImmutableMap.Builder<Block, Block>()
                .put(OLIBANUM_WOOD.get(), STRIPPED_OLIBANUM_WOOD.get())
                .put(OLIBANUM_LOG.get(), STRIPPED_OLIBANUM_LOG.get())
                .put(MYRRH_WOOD.get(), STRIPPED_MYRRH_WOOD.get())
                .put(MYRRH_LOG.get(), STRIPPED_MYRRH_LOG.get())
                .build();

        Block block = STRIPPABLES.get(originalState);
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    public static void register() {
    }
}
