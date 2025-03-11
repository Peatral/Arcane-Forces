package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;


public class ModCreativeTabs {

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MAIN_TAB = REGISTRATE.get().object("arcane_forces").defaultCreativeTab(builder -> builder.icon(() -> new ItemStack(ModItems.RITUAL_DAGGER.get()))).register();

    public static void register(IEventBus eventBus) {
        eventBus.addListener(BuildCreativeModeTabContentsEvent.class, ModCreativeTabs::buildCreativeTab);
    }

    public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTab().equals(MAIN_TAB.get()))
            return;

        event.getParameters().holders().lookupOrThrow(Registries.ENCHANTMENT).get(ModTags.Enchantments.SPELLS).ifPresent(holders -> holders.forEach(enchantmentHolder -> {
            ItemStack stack = new ItemStack(ModItems.VOODOO_DOLL.get());
            stack.enchant(enchantmentHolder, 1);
            event.accept(stack);
        }));
    }
}
