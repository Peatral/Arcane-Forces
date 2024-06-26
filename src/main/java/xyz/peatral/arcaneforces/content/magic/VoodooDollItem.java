package xyz.peatral.arcaneforces.content.magic;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import xyz.peatral.arcaneforces.content.magic.spells.EnchantmentSpellEffect;

public class VoodooDollItem extends Item {
    public VoodooDollItem() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (!stack.has(DataComponents.ENCHANTMENTS))
            return InteractionResultHolder.fail(stack);

        boolean castedCurse = false;

        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        var lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup != null) {
            itemenchantments = stack.getAllEnchantments(lookup);
        }

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            for (TypedDataComponent<?> typedDataComponent : holder.value().effects()) {
                if (typedDataComponent.value() instanceof EnchantmentSpellEffect effect) {
                    effect.type().value().castCurse(level, player, player);
                    castedCurse = true;
                }
            }
        }

        if (!castedCurse)
            return InteractionResultHolder.fail(stack);

        return player.isCreative() ? InteractionResultHolder.consume(stack) : InteractionResultHolder.consume(new ItemStack(Items.AIR));
    }
}
