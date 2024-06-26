package xyz.peatral.arcaneforces.content.magic.spells.spelltypes;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xyz.peatral.arcaneforces.ModMobEffects;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

public class FireSpell extends Spell {
    public FireSpell() {
        super("fire", 5, 50);
    }

    @Override
    public void castCurse(Level world, Player target, Player attacker) {
        target.addEffect(new MobEffectInstance(ModMobEffects.FIRE, 20 * 90, 0));
    }


}
