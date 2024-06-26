package xyz.peatral.arcaneforces.client.gui.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class RankbarTooltip implements TooltipComponent {
    float percentage;
    int rank;

    public RankbarTooltip(float percentage, int rank) {
        this.percentage = percentage;
        this.rank = rank;
    }
}
