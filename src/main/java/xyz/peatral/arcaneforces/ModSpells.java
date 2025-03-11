package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.DeathSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.FireSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.FlightSpell;
import xyz.peatral.arcaneforces.content.magic.spells.spelltypes.PoisonSpell;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModSpells {

    public static final ResourceKey<Registry<Spell>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "curses"));
    public static final Registry<Spell> REGISTRY = new RegistryBuilder<>(REGISTRY_KEY).sync(true).create();
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(REGISTRY, Main.MOD_ID);

    public static final RegistryEntry<Spell, DeathSpell> DEATH = spell("death", DeathSpell::new, "Curse of Death");
    public static final RegistryEntry<Spell, FlightSpell> FLIGHT = spell("flight", FlightSpell::new, "Blessing of Flight");
    public static final RegistryEntry<Spell, FireSpell> FIRE = spell("fire", FireSpell::new, "Curse of Fire");
    public static final RegistryEntry<Spell, PoisonSpell> POISON = spell("poison", PoisonSpell::new, "Curse of Poison");

    private static <T extends Spell> RegistryEntry<Spell, T> spell(String name, NonNullSupplier<T> factory, String translation) {
        return REGISTRATE.get().generic(name, REGISTRY_KEY, factory).lang(Spell::getDescriptionId, translation).register();
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
        eventBus.addListener(NewRegistryEvent.class, event -> event.register(REGISTRY));
    }
}
