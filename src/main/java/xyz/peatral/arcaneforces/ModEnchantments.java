package xyz.peatral.arcaneforces;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.peatral.arcaneforces.content.magic.spells.EnchantmentSpellEffect;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> POISON_CURSE = key("poison_curse");
    public static final ResourceKey<Enchantment> FIRE_CURSE = key("fire_curse");
    public static final ResourceKey<Enchantment> DEATH_CURSE = key("death_curse");
    public static final ResourceKey<Enchantment> FLIGHT_BLESSING = key("flight_blessing");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        registerSimple(context, POISON_CURSE, ModSpells.POISON);
        registerSimple(context, FIRE_CURSE, ModSpells.FIRE);
        registerSimple(context, DEATH_CURSE, ModSpells.DEATH);
        registerSimple(context, FLIGHT_BLESSING, ModSpells.FLIGHT);
    }

    private static void registerSimple(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Holder<Spell> holder) {
        register(
                context,
                key,
                new Enchantment.Builder(
                        Enchantment.definition(
                                BuiltInRegistries.ITEM.getOrCreateTag(ModTags.Items.SPELL_HOLDERS),
                                BuiltInRegistries.ITEM.getOrCreateTag(ModTags.Items.SPELL_HOLDERS),
                                3,
                                1,
                                new Enchantment.Cost(10, 20),
                                new Enchantment.Cost(10, 20),
                                5,
                                EquipmentSlotGroup.HAND
                        )
                )
                        .withSpecialEffect(ModEnchantmentEffects.SPELL.get(), new EnchantmentSpellEffect(holder))
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name));
    }
}
