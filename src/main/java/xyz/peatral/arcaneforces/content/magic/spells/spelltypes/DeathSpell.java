package xyz.peatral.arcaneforces.content.magic.spells.spelltypes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xyz.peatral.arcaneforces.ModDamageTypes;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

public class DeathSpell extends Spell {

    public DeathSpell() {
        super("death", 1, 1000);
    }


    @Override
    public void castCurse(Level level, Player target, Player attacker) {
        target.hurt(
                new DamageSource(
                        level
                                .registryAccess()
                                .registry(Registries.DAMAGE_TYPE)
                                .orElseThrow()
                                .getHolderOrThrow(ModDamageTypes.DEATH_CURSE),
                        target,
                        attacker
                ),
                Float.MAX_VALUE
        );
    }
}
