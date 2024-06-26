package xyz.peatral.arcaneforces.content.magic.spells.mobeffects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.NeoForgeMod;
import xyz.peatral.arcaneforces.Main;

import java.util.Set;

public class FlightSpellMobEffect extends MobEffect {
    public FlightSpellMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        this.addAttributeModifier(
                NeoForgeMod.CREATIVE_FLIGHT,
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "blessing.flight"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
    }
}
