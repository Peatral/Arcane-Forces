package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> DEATH_CURSE = key("death_curse");
    public static final ResourceKey<DamageType> RITUAL_SACRIFICE = key("ritual_sacrifice");

    public static void bootstrap(BootstrapContext<DamageType> pContext) {
        pContext.register(DEATH_CURSE, new DamageType("death_curse", 0.1f));
        pContext.register(RITUAL_SACRIFICE, new DamageType("ritual_sacrifice", 0.1f));
    }

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
    }
}
