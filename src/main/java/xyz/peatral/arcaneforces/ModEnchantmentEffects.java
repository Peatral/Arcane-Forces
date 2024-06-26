package xyz.peatral.arcaneforces;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.magic.spells.EnchantmentSpellEffect;

public class ModEnchantmentEffects {
    private static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENTS = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Main.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentSpellEffect>> SPELL = ENCHANTMENT_COMPONENTS.register("spell",
            () -> DataComponentType.<EnchantmentSpellEffect>builder().persistent(EnchantmentSpellEffect.CODEC).cacheEncoding().build());

    public static void register(IEventBus eventBus) {
        ENCHANTMENT_COMPONENTS.register(eventBus);
    }
}
