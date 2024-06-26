package xyz.peatral.arcaneforces;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.FireSpellMobEffect;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.FlightSpellMobEffect;
import xyz.peatral.arcaneforces.content.magic.spells.mobeffects.PoisonSpellMobEffect;


public class ModMobEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Main.MOD_ID);

    public static final Holder<MobEffect> FLIGHT = MOB_EFFECTS.register("flight_blessing", FlightSpellMobEffect::new);
    public static final Holder<MobEffect> FIRE = MOB_EFFECTS.register("fire_curse", FireSpellMobEffect::new);
    public static final Holder<MobEffect> POISON = MOB_EFFECTS.register("poison_curse", PoisonSpellMobEffect::new);

    public static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }
}
