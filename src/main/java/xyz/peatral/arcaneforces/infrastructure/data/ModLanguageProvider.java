package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.ModMobEffects;
import xyz.peatral.arcaneforces.ModSpells;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

public class ModLanguageProvider extends LanguageProvider {
    private final String modid;
    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modid = modid;
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + modid + ".main", "Arcane Forces");

        add(ModItems.RITUAL_DAGGER.get(), "Ritual Dagger");
        add(ModItems.CURSED_RING.get(), "Cursed Ring");
        add(ModItems.VOODOO_DOLL.get(), "Voodoo Doll");
        add(ModItems.FRAGRANT_RESIN.get(), "Fragrant Resin");

        add(ModSpells.FIRE.get(), "Curse of Fire");
        add(ModSpells.DEATH.get(), "Curse of Death");
        add(ModSpells.POISON.get(), "Curse of Poison");
        add(ModSpells.FLIGHT.get(), "Blessing of Flight");

        addEnchantment("fire_curse", "Curse of Fire");
        addEnchantment("death_curse", "Curse of Death");
        addEnchantment("poison_curse", "Curse of Poison");
        addEnchantment("flight_blessing", "Blessing of Flight");

        add(ModMobEffects.FLIGHT.value(), "Blessing of Flight");
        add(ModMobEffects.FIRE.value(), "Curse of Fire");
        add(ModMobEffects.POISON.value(), "Curse of Poison");

        add(ModBlocks.OLIBANUM_SAPLING.block().get(), "Olibanum Sapling");
        add(ModBlocks.MYRRH_SAPLING.block().get(), "Myrrh Sapling");
        add(ModBlocks.OLIBANUM_LEAVES.block().get(), "Olibanum Leaves");
        add(ModBlocks.MYRRH_LEAVES.block().get(), "Myrrh Leaves");

        add(ModBlocks.OLIBANUM_LOG.block().get(), "Olibanum Log");
        add(ModBlocks.MYRRH_LOG.block().get(), "Myrrh Log");
        add(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get(), "Stripped Olibanum Log");
        add(ModBlocks.STRIPPED_MYRRH_LOG.block().get(), "Stripped Myrrh Log");
        add(ModBlocks.OLIBANUM_WOOD.block().get(), "Olibanum Wood");
        add(ModBlocks.MYRRH_WOOD.block().get(), "Myrrh Wood");
        add(ModBlocks.STRIPPED_OLIBANUM_WOOD.block().get(), "Stripped Olibanum Wood");
        add(ModBlocks.STRIPPED_MYRRH_WOOD.block().get(), "Stripped Myrrh Wood");

        add("death.attack.ritual_sacrifice", "%1$s was sacrificed");
        add("death.attack.ritual_sacrifice.player", "%1$s was sacrificed by %2$s");
        add("death.attack.ritual_sacrifice.item", "%1$s was sacrificed by %2$s using %3$s");
        //add("death.attack.death_curse", "%1$s was cursed to death");
        add("death.attack.death_curse", "%1$s was cursed to death by %2$s");
        add("death.attack.death_curse.item", "%1$s was cursed to death by %2$s using %3$s");
        add("death.attack.death_curse.player", "%1$s was cursed to death by %2$s");

        add(modid + ".tooltip.rank", "Rank %1$d: %2$s/%3$s");
        add(modid + ".tooltip.ownedby", "Owned by: %1$s");
        add(modid + ".tooltip.target", "Target: %1$s");

        add(modid + ".ranking.toast.title", "RANK UP!");
        add(modid + ".ranking.toast.description", "%1$s [%2$s]");

        add("key.categories." + modid, "Arcane Forces");
        add("key." + modid + ".cast_spell", "Cast Invocation");

        add(modid + ".networking.failed", "Error while networking!");
    }

    public void add(Spell key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addEnchantment(String key, String name) {
        add("enchantment." + modid + "." + key, name);
    }
}
