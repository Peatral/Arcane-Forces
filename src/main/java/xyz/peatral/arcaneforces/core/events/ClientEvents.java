package xyz.peatral.arcaneforces.core.events;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import xyz.peatral.arcaneforces.*;
import xyz.peatral.arcaneforces.api.events.RankUpEvent;
import xyz.peatral.arcaneforces.client.gui.toasts.RankupToast;
import xyz.peatral.arcaneforces.client.gui.tooltip.RankbarTooltip;
import xyz.peatral.arcaneforces.content.ranking.IRanking;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void heartType(PlayerHeartTypeEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.POISON)) {
            event.setType(Gui.HeartType.POISIONED);
        }
    }

    @SubscribeEvent
    public static void rankUp(RankUpEvent event) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (event.ownerUUID == null || localPlayer == null || !event.ownerUUID.equals(localPlayer.getUUID())) return;

        Minecraft.getInstance().getToasts().addToast(new RankupToast(event.name, event.rank, event.icon));
    }

    @SubscribeEvent
    public static void itemToolTip(final ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.has(ModDataComponents.TARGET.get())) {
            event.getToolTip().add(
                    1,
                    Component.translatable(Main.MOD_ID + ".tooltip.target", Utils.getName(stack.get(ModDataComponents.TARGET.get())))
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
            );
        }

        IRanking cap = stack.getCapability(ModCapabilities.RANKING_CAPABILITY);
        if (cap == null) return;

        event.getToolTip().add(Component.literal(""));
        event.getToolTip().add(
                Component.translatable(
                        Main.MOD_ID + ".tooltip.rank",
                        cap.getRank(),
                        cap.getAffinityInCurrentRank(),
                        cap.getAffinityForNextRank()
                ).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY))));
        event.getToolTip().add(Component.literal(Main.MOD_ID + ":RANKBAR_COMPONENT"));

        if (cap.hasOwner() && InventoryScreen.hasShiftDown()) {
            event.getToolTip().add(Component.literal(""));
            event.getToolTip().add(
                    Component.translatable(
                            Main.MOD_ID + ".tooltip.ownedby",
                            Utils.getName(cap.getOwner())
                    ).withStyle(cap.getOwner().id().get().equals(event.getEntity().getUUID()) ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
    }

    @SubscribeEvent
    public static void onRenderTooltip(final RenderTooltipEvent.GatherComponents event) {
        int idx = -1;
        List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
        for (int i = 0; i < components.size(); i++) {
            Optional<FormattedText> text = components.get(i).left();
            if (text.isPresent() && text.get() instanceof Component comp && comp.getContents() instanceof PlainTextContents.LiteralContents tc) {
                if ((Main.MOD_ID + ":RANKBAR_COMPONENT").equals(tc.text())) {
                    idx = i;
                    components.remove(idx);
                    break;
                }
            }
        }
        if (idx == -1) return;

        ItemStack stack = event.getItemStack();
        IRanking cap = stack.getCapability(ModCapabilities.RANKING_CAPABILITY);
        if (cap == null) return;

        components.add(idx, Either.right(new RankbarTooltip(cap.getAffinityForDisplay(), cap.getRank())));

    }
}
