package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.FireSpellMobEffect;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.FlightSpellMobEffect;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.PoisonSpellMobEffect;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;


public class ModMobEffects {

    public static final RegistryEntry<MobEffect, FlightSpellMobEffect> FLIGHT = effect("flight_blessing", FlightSpellMobEffect::new, "Blessing of Flight");
    public static final RegistryEntry<MobEffect, FireSpellMobEffect> FIRE = effect("fire_curse", FireSpellMobEffect::new, "Curse of Fire");
    public static final RegistryEntry<MobEffect, PoisonSpellMobEffect> POISON = effect("poison_curse", PoisonSpellMobEffect::new, "Curse of Poison");

    private static <T extends MobEffect> RegistryEntry<MobEffect, T> effect(String name, NonNullSupplier<T> factory, String translation) {
        return REGISTRATE.get().generic(name, Registries.MOB_EFFECT, factory).lang(MobEffect::getDescriptionId, translation).register();
    }

    public static void register() {
    }
}
