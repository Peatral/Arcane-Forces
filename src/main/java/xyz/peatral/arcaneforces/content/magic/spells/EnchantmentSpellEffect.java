package xyz.peatral.arcaneforces.content.magic.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

public record EnchantmentSpellEffect(Holder<Spell> type) {
    public static final Codec<EnchantmentSpellEffect> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Spell.CODEC.fieldOf("type").forGetter(EnchantmentSpellEffect::type)
            ).apply(instance, EnchantmentSpellEffect::new)
    );
}
