package xyz.peatral.arcaneforces;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import xyz.peatral.arcaneforces.content.ranking.ComponentRanking;
import xyz.peatral.arcaneforces.content.ranking.IRanking;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModCapabilities {

    public static final ItemCapability<IRanking, Void> RANKING_CAPABILITY =
            ItemCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "ranking"),
                    IRanking.class
            );

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerItem(
                RANKING_CAPABILITY,
                (stack, context) -> new ComponentRanking(stack),
                ModItems.RITUAL_DAGGER.get(),
                ModItems.CURSED_RING.get()
        );
    }
}
