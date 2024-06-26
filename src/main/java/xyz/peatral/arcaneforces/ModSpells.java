package xyz.peatral.arcaneforces;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.DeathSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.FireSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.FlightSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.PoisonSpell;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModSpells {

    public static final ResourceKey<Registry<Spell>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "curses"));
    public static final Registry<Spell> REGISTRY = new RegistryBuilder<>(REGISTRY_KEY).sync(true).create();
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(REGISTRY, Main.MOD_ID);

    public static final DeferredHolder<Spell, DeathSpell> DEATH = SPELLS.register("death", DeathSpell::new);
    public static final DeferredHolder<Spell, FlightSpell> FLIGHT = SPELLS.register("flight", FlightSpell::new);
    public static final DeferredHolder<Spell, FireSpell> FIRE = SPELLS.register("fire", FireSpell::new);
    public static final DeferredHolder<Spell, PoisonSpell> POISON = SPELLS.register("poison", PoisonSpell::new);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerRegistry(NewRegistryEvent event) {
        event.register(REGISTRY);
    }
}
