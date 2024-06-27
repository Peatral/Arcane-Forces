package xyz.peatral.arcaneforces;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.magic.CursedRingItem;
import xyz.peatral.arcaneforces.content.magic.RitualDaggerItem;
import xyz.peatral.arcaneforces.content.magic.VoodooDollItem;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MOD_ID);

    public static final DeferredHolder<Item, RitualDaggerItem> RITUAL_DAGGER = ITEMS.register("ritualdagger", RitualDaggerItem::new);
    public static final DeferredHolder<Item, CursedRingItem> CURSED_RING = ITEMS.register("cursedring", CursedRingItem::new);
    public static final DeferredHolder<Item, VoodooDollItem> VOODOO_DOLL = ITEMS.register("voodoodoll", VoodooDollItem::new);
    public static final DeferredHolder<Item, Item> FRAGRANT_RESIN = ITEMS.register("fragrant_resin", () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    private static void registerClientStuff(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.RITUAL_DAGGER.get(), ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hastarget"), (stack, level, entity, seed) -> stack.has(ModDataComponents.TARGET.get()) ? 1 : 0);
    }
}
