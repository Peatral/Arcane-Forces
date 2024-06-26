package xyz.peatral.arcaneforces.content.magic.spells.spelltypes;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xyz.peatral.arcaneforces.ModMobEffects;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

public class PoisonSpell extends Spell {

    public PoisonSpell() {
        super("poison", 1, 100);
    }

    @Override
    public void castCurse(Level level, Player target, Player attacker) {
        target.addEffect(new MobEffectInstance(ModMobEffects.POISON, 20*90, 0));
    }
}
