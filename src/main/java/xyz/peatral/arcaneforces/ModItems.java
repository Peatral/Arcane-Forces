package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.peatral.arcaneforces.content.magic.CursedRingItem;
import xyz.peatral.arcaneforces.content.magic.RitualDaggerItem;
import xyz.peatral.arcaneforces.content.magic.VoodooDollItem;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;


public class ModItems {
    public static final ItemEntry<RitualDaggerItem> RITUAL_DAGGER = REGISTRATE.get()
            .item("ritualdagger", properties -> new RitualDaggerItem())
            .model((context, provider) -> provider.basicItem(
                    context.get()).override()
                            .predicate(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hastarget"), 1)
                            .model(provider.basicItem(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "ritualdagger_bloody")))
                            .end()
            )
            .lang("Ritual Dagger").register();
    public static final ItemEntry<CursedRingItem> CURSED_RING = REGISTRATE.get().item("cursedring", properties -> new CursedRingItem()).lang("Cursed Ring").register();
    public static final ItemEntry<VoodooDollItem> VOODOO_DOLL = REGISTRATE.get().item("voodoodoll", properties -> new VoodooDollItem()).tab(null).lang("Voodoo Doll").register();
    public static final ItemEntry<Item> FRAGRANT_RESIN = REGISTRATE.get().item("fragrant_resin", Item::new).lang("Fragrant Resin").register();


    public static void register(IEventBus eventBus) {
        eventBus.addListener(FMLClientSetupEvent.class, ModItems::registerClientStuff);
    }

    private static void registerClientStuff(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.RITUAL_DAGGER.get(), ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hastarget"), (stack, level, entity, seed) -> stack.has(ModDataComponents.TARGET.get()) ? 1 : 0);
    }
}
