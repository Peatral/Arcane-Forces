package xyz.peatral.arcaneforces.core.events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.peatral.arcaneforces.ModDataAttachments;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.Utils;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;
import xyz.peatral.arcaneforces.content.magic.spells.CastSpellPayload;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (ClientModEvents.CAST_SPELL_MAPPING.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player == null)
                continue;
            ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
            ResolvableProfile owner;
            if (offHand.has(ModDataComponents.OWNER) && (owner = offHand.get(ModDataComponents.OWNER)) != null && owner.id().isPresent() && owner.id().get().equals(player.getUUID())) {
                continue;
            }
            ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            ResolvableProfile targetUuid = mainHand.get(ModDataComponents.TARGET);
            if (targetUuid == null)
                continue;
            SpellSelector spellSelector = player.getData(ModDataAttachments.SPELL_SELECTOR);
            List<Holder<Spell>> spells = Utils.getAllSpells(offHand);
            int slot = spellSelector.slot();
            if (slot < 0 || slot > spells.size())
                continue;
            Holder<Spell> selectedSpell = spells.get(slot);
            PacketDistributor.sendToServer(new CastSpellPayload(selectedSpell, targetUuid));
        }
    }

    @SubscribeEvent
    public static void onMouseEvent(InputEvent.MouseScrollingEvent event) {
        if (event.getScrollDeltaY() != 0) {
            Player player = Minecraft.getInstance().player;
            ItemStack stack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (stack.getItem().equals(ModItems.CURSED_RING.get())) {
                if (player.isShiftKeyDown()) {
                    SpellSelector c = player.getData(ModDataAttachments.SPELL_SELECTOR.get());
                    c = new SpellSelector(
                            (c.slot() - (int) event.getScrollDeltaY() + c.slotAmount()) % c.slotAmount(),
                            c.slotAmount(),
                            c.cooldown(),
                            c.cooldownMax()
                    );
                    player.setData(ModDataAttachments.SPELL_SELECTOR.get(), c);
                    PacketDistributor.sendToServer(c);
                    event.setCanceled(true);
                }
            }
        }
    }
}
