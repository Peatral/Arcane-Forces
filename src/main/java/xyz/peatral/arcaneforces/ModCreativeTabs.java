package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Main.MOD_ID + ".main"))
            .icon(() -> new ItemStack(ModItems.RITUAL_DAGGER))
            .displayItems((params, output) -> {
                output.accept(ModItems.RITUAL_DAGGER.get());
                output.accept(ModItems.CURSED_RING.get());
                output.accept(ModItems.VOODOO_DOLL.get());

                output.accept(ModItems.FRAGRANT_RESIN.get());
                output.accept(ModBlocks.INCENSE_STICK.block().get());

                output.accept(ModBlocks.OLIBANUM_SAPLING.block().get());
                output.accept(ModBlocks.MYRRH_SAPLING.block().get());

                output.accept(ModBlocks.OLIBANUM_LOG.block().get());
                output.accept(ModBlocks.OLIBANUM_WOOD.block().get());
                output.accept(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get());
                output.accept(ModBlocks.STRIPPED_OLIBANUM_WOOD.block().get());
                output.accept(ModBlocks.MYRRH_LOG.block().get());
                output.accept(ModBlocks.MYRRH_WOOD.block().get());
                output.accept(ModBlocks.STRIPPED_MYRRH_LOG.block().get());
                output.accept(ModBlocks.STRIPPED_MYRRH_WOOD.block().get());
                output.accept(ModBlocks.OLIBANUM_LEAVES.block().get());
                output.accept(ModBlocks.MYRRH_LEAVES.block().get());

            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

    @SubscribeEvent
    public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTab().equals(MAIN_TAB.get()))
            return;

        event.getParameters().holders().lookupOrThrow(Registries.ENCHANTMENT).get(ModTags.Enchantments.SPELLS).ifPresent(holders -> holders.forEach(enchantmentHolder -> {
            ItemStack stack = new ItemStack(ModItems.VOODOO_DOLL);
            stack.enchant(enchantmentHolder, 1);
            event.accept(stack);
        }));
    }
}
