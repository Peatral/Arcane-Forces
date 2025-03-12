package xyz.peatral.arcaneforces;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;
import xyz.peatral.arcaneforces.content.magic.spells.CastSpellPayload;
import xyz.peatral.arcaneforces.content.shrines.SyncGraphPayload;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModPayloads {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SpellSelector.TYPE,
                SpellSelector.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SpellSelector::handlePacketClient,
                        SpellSelector::handlePacketServer
                )
        );
        registrar.playToServer(
                CastSpellPayload.TYPE,
                CastSpellPayload.STREAM_CODEC,
                CastSpellPayload::handlePacketServer
        );

        registrar.playToClient(
                SyncGraphPayload.TYPE,
                SyncGraphPayload.STREAM_CODEC,
                SyncGraphPayload::handlePacketClient
        );
    }
}
