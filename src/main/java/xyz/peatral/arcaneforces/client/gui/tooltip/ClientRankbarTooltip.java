package xyz.peatral.arcaneforces.client.gui.tooltip;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import xyz.peatral.arcaneforces.Main;

public class ClientRankbarTooltip implements ClientTooltipComponent {
    private static final ResourceLocation SPRITE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "container/ranking/bar");
    private final float percentage;

    public ClientRankbarTooltip(RankbarTooltip tooltip) {
        this.percentage = tooltip.percentage;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font pFont) {
        return 64;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        int x = pX;
        int y = pY + 3;
        GlStateManager._enableBlend();
        pGuiGraphics.blitSprite(SPRITE, 256, 256, 0, 0, x, y, 64, 5);
        pGuiGraphics.blitSprite(SPRITE, 256, 256, 0, 5, x, y, (int) (64 * this.percentage), 5);
        pGuiGraphics.blitSprite(SPRITE, 256, 256, 0, 10, x, y, 64, 5);
        GlStateManager._disableBlend();
    }
}
