package xyz.peatral.arcaneforces;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import xyz.peatral.arcaneforces.client.gui.tooltip.ClientRankbarTooltip;
import xyz.peatral.arcaneforces.client.gui.tooltip.RankbarTooltip;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModTooltips {

    @SubscribeEvent
    public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(RankbarTooltip.class, ClientRankbarTooltip::new);
    }
}
