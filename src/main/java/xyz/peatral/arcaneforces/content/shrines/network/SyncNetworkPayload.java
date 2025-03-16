package xyz.peatral.arcaneforces.content.shrines.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.peatral.arcaneforces.Main;

public record SyncNetworkPayload(ResourceKey<Level> level, ShrineNetwork network) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncNetworkPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "sync_graph"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncNetworkPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), SyncNetworkPayload::level,
            ShrineNetwork.STREAM_CODEC, SyncNetworkPayload::network,
            SyncNetworkPayload::new
    );

    public static void handlePacketClient(final SyncNetworkPayload data, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientShrineSavedData.setNetwork(data.level, data.network);
        }).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MOD_ID + ".networking.failed", e.getMessage()));
            return null;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
