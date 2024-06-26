package xyz.peatral.arcaneforces.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.timings.TimeTracker;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModDataAttachments;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.Utils;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;
import xyz.peatral.arcaneforces.content.magic.spells.Spell;

import java.util.List;

public class CursedRingGuiLayer implements LayeredDraw.Layer {

    private static ResourceLocation HOTBAR = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/hotbar_ring");
    private static ResourceLocation SELECTOR = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/hotbar_selector");
    private static ResourceLocation COOLDOWN_OVERLAY = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/hotbar_cooldown");

    int selectedSlot = -1;
    int slotAmount = 5;
    int cooldown = 0;
    int cooldownMax = 0;

    public static final int fadeDuration = 40;
    public static final int hotbarX = 4;
    public static final int hotbarY = 4;



    TimeTracker timeTracker = new TimeTracker();

    public void setSlot(int slot) {
        if (this.selectedSlot != slot) {
            this.selectedSlot = slot;
            //this.timeTracker.trackStart(Minecraft.getInstance().level);
        }
    }

    public void setSlotAmount(int slotAmount) {
        this.slotAmount = slotAmount;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setCooldownMax(int cooldownMax) {
        this.cooldownMax = cooldownMax;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        SpellSelector c = player.getData(ModDataAttachments.SPELL_SELECTOR.get());
        setSlot(c.slot());
        setSlotAmount(c.slotAmount());
        setCooldown(c.cooldown());
        setCooldownMax(c.cooldownMax());

        ItemStack stack = player.getItemInHand(InteractionHand.OFF_HAND);
        //int cursedRingSlot = BaublesApi.isBaubleEquipped(mc.player, ArcaneForcesItems.cursedRing);
        //if (cursedRingSlot != -1) {
        //    ItemStack stack = BaublesApi.getBaublesHandler(mc.player).getStackInSlot(cursedRingSlot);
        if (!stack.getItem().equals(ModItems.CURSED_RING.get()))
            return;

        GlStateManager._enableBlend();
        for (int i = 0; i < slotAmount; i++) {
            if (i == 0) {
                guiGraphics.blitSprite(HOTBAR, 22, 62, 0, 0, hotbarX, hotbarY,  22, 21);
            } else if (i == slotAmount - 1) {
                guiGraphics.blitSprite(HOTBAR, 22, 62, 0, 41, hotbarX, hotbarY + 1 + 20 * i,  22, 21);
            } else {
                guiGraphics.blitSprite(HOTBAR, 22, 62, 0, 21, hotbarX, hotbarY + 1 + 20 * i,  22, 21);
            }
        }

        List<Holder<Spell>> spells = Utils.getAllSpells(stack);
        for (int i = 0; i < spells.size() && i < slotAmount; i++) {
            guiGraphics.blitSprite(spells.get(i).value().iconResource, hotbarX + 3, hotbarY + 3 + 20 * i,  16, 16);
        }

        //renderCooldown(stack);
        guiGraphics.blitSprite(SELECTOR, hotbarX - 1, hotbarY - 1 + selectedSlot * (16 + 4), 24, 24);
        GlStateManager._disableBlend();
        //renderSelectorName(stack);
    }

    /*

    private void renderCooldown(ItemStack stack) {
        mc.getTextureManager().bindTexture(COOLDOWN_OVERLAY);
        for (int i = 0; i < ItemCursedRing.getCurseAmount(stack); i++) {
            int c = (int)Math.ceil(((float)cooldown/(float)cooldownMax * 16));
            drawModalRectWithCustomSizedTexture(hotbarX + 3, hotbarY + 3 + i * (16 + 4), c, 16, c, 16, 16, 16);

        }
        mc.getTextureManager().bindTexture(Gui.ICONS);
    }

    private void renderSelectorName(ItemStack stack) {
        if (ItemCursedRing.hasCurse(stack, selectedSlot)) {
            CurseType curse = ItemCursedRing.getCurse(stack, selectedSlot);

            int remainingHighlightTicks = (int) (fadeDuration-timeTracker.getPassedTime(mc.world));

            if (remainingHighlightTicks > 0) {
                String s = curse.getLocalizedName();

                ScaledResolution scaledRes = new ScaledResolution(mc);
                int i = (scaledRes.getScaledWidth() - mc.fontRenderer.getStringWidth(s)) / 2;
                int j = scaledRes.getScaledHeight() - 59 - 12;

                if (!this.mc.playerController.shouldDrawHUD()) {
                    j += 14;
                }

                int k = (int) ((float) remainingHighlightTicks * 256.0F / 10.0F);

                if (k > 255) {
                    k = 255;
                }

                if (k > 0) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    mc.fontRenderer.drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    */

}
