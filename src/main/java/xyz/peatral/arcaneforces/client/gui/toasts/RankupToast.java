package xyz.peatral.arcaneforces.client.gui.toasts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import xyz.peatral.arcaneforces.Main;

@OnlyIn(Dist.CLIENT)
public class RankupToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "toast/rankup");
    private static final Component TITLE_TEXT = Component.translatable(Main.MOD_ID + ".ranking.toast.title");

    private final Component name;
    private final int rank;
    private final ItemStack icon;

    public RankupToast(Component name, int rank, ItemStack icon) {
        this.name = name;
        this.rank = rank;
        this.icon = icon;
    }

    @Override
    public Visibility render(GuiGraphics pGuiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
        pGuiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, TITLE_TEXT, 35, 7, -11534256, false);
        pGuiGraphics.drawString(pToastComponent.getMinecraft().font, Component.translatable(Main.MOD_ID + ".ranking.toast.description", name != null ? name : Component.literal("undefined"), rank), 35, 18, -16777216, false);
        pGuiGraphics.renderFakeItem(icon, 8, 8);

        return pTimeSinceLastVisible >= 5000.0 * pToastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
