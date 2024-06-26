package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Enchantments {
        public static final TagKey<Enchantment> SPELLS = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "spells"));
    }

    public static class Blocks {
        public static final TagKey<Block> INCENSE_LOGS = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "incense_logs"));
        public static final TagKey<Block> OLIBANUM_LOGS = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "olibanum_logs"));
        public static final TagKey<Block> MYRRH_LOGS = BlockTags.create( ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "myrrh_logs"));
    }

    public static class Items {
        public static final TagKey<Item> SPELL_HOLDERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "enchantable/spell_holders"));
        public static final TagKey<Item> INCENSE_LOGS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "incense_logs"));
        public static final TagKey<Item> OLIBANUM_LOGS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "olibanum_logs"));
        public static final TagKey<Item> MYRRH_LOGS = ItemTags.create( ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "myrrh_logs"));
    }
}
