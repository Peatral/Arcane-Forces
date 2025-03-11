package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import xyz.peatral.arcaneforces.content.magic.spells.EnchantmentSpellEffect;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModEnchantmentEffects {
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EnchantmentSpellEffect>> SPELL = REGISTRATE.get().generic(
            "spell",
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            () -> DataComponentType.<EnchantmentSpellEffect>builder().persistent(EnchantmentSpellEffect.CODEC).cacheEncoding().build()
    ).register();

    public static void register() {
    }
}
