package xyz.peatral.arcaneforces;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import xyz.peatral.arcaneforces.api.events.RankUpEvent;
import xyz.peatral.arcaneforces.content.magic.spells.EnchantmentSpellEffect;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;
import xyz.peatral.arcaneforces.content.ranking.IRanking;

import java.util.*;

public class Utils {
    public static List<Holder<Spell>> getAllSpells(ItemStack stack) {
        ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        var lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup != null) {
            itemenchantments = stack.getAllEnchantments(lookup);
        }
        Set<Holder<Spell>> spells = new HashSet<>();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            for (TypedDataComponent<?> typedDataComponent : holder.value().effects()) {
                if (typedDataComponent.value() instanceof EnchantmentSpellEffect effect) {
                    spells.add(effect.type());
                }
            }
        }
        List<Holder<Spell>> spellList = new ArrayList<>(spells);
        spellList.sort(Comparator.comparing(spellHolder -> spellHolder.value().getDescriptionId()));
        return spellList;
    }


    public static void modifyAffinity(ItemStack stack, Player player, int amount) {
        IRanking ranking = stack.getCapability(ModCapabilities.RANKING_CAPABILITY);
        if (ranking == null)
            return;
        if (ranking.hasOwner() && !ranking.getOwner().id().get().equals(player.getUUID()))
            return;

        int stacklevel = ranking.getRank();
        ranking.setOwner(ranking.hasOwner() ? ranking.getOwner() : new ResolvableProfile(player.getGameProfile()));
        ranking.addAffinity(amount);
        int newstacklevel = ranking.getRank();
        if (stacklevel < newstacklevel) {
            NeoForge.EVENT_BUS.post(
                    new RankUpEvent(
                            newstacklevel,
                            ranking.getOwner(),
                            stack.getHoverName(),
                            stack
                    )
            );
        }
    }

    public static Component getName(ResolvableProfile profile) {
        return profile != null && profile.name().isPresent()
                ? Component.literal(profile.name().get())
                : Component.literal("Unknown Profile");
    }

    public static void verifyResolvableProfileComponent(ItemStack stack, DataComponentType<ResolvableProfile> componentType) {
        ResolvableProfile resolvableprofile = stack.get(componentType);
        if (resolvableprofile != null && !resolvableprofile.isResolved()) {
            resolvableprofile.resolve()
                    .thenAcceptAsync(profile -> stack.set(componentType, profile), SkullBlockEntity.CHECKED_MAIN_THREAD_EXECUTOR);
        }
    }
}
