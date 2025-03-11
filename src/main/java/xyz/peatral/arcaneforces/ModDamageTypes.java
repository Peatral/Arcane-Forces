package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> DEATH_CURSE = key("death_curse");
    public static final ResourceKey<DamageType> RITUAL_SACRIFICE = key("ritual_sacrifice");

    static {
        REGISTRATE.get().addRawLang("death.attack.ritual_sacrifice", "%1$s was sacrificed");
        REGISTRATE.get().addRawLang("death.attack.ritual_sacrifice.player", "%1$s was sacrificed by %2$s");
        REGISTRATE.get().addRawLang("death.attack.ritual_sacrifice.item", "%1$s was sacrificed by %2$s using %3$s");

        REGISTRATE.get().addRawLang("death.attack.death_curse", "%1$s was cursed to death by %2$s");
        REGISTRATE.get().addRawLang("death.attack.death_curse.item", "%1$s was cursed to death by %2$s using %3$s");
        REGISTRATE.get().addRawLang("death.attack.death_curse.player", "%1$s was cursed to death by %2$s");
    }

    public static void bootstrap(BootstrapContext<DamageType> pContext) {
        pContext.register(DEATH_CURSE, new DamageType("death_curse", 0.1f));
        pContext.register(RITUAL_SACRIFICE, new DamageType("ritual_sacrifice", 0.1f));
    }

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
    }
}
