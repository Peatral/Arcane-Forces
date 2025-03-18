package xyz.peatral.arcaneforces;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBufferCache;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Main.MOD_ID, dist = Dist.CLIENT)
public class MainClient {
    public MainClient(IEventBus modBus) {
        modBus.addListener(MainClient::clientInit);
        modBus.addListener(FMLClientSetupEvent.class, ModItems::registerClientStuff);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        SuperByteBufferCache.getInstance().registerCompartment(CachedBuffers.PARTIAL);
        ModPartialModels.init();
    }
}
