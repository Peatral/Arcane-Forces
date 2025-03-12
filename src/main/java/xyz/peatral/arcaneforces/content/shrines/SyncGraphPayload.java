package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.peatral.arcaneforces.Main;

import java.util.HashMap;
import java.util.HashSet;

public record SyncGraphPayload(ResourceKey<Level> level, Graph<BlockPos> graph) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncGraphPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "sync_graph"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Graph<BlockPos>> GRAPH_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, BlockPos.STREAM_CODEC), Graph::getNodes,
            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, ByteBufCodecs.collection(HashSet::new, BlockPos.STREAM_CODEC)), Graph::getAdjacency,
            Graph::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncGraphPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), SyncGraphPayload::level,
            GRAPH_STREAM_CODEC, SyncGraphPayload::graph,
            SyncGraphPayload::new
    );

    public static void handlePacketClient(final SyncGraphPayload data, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientShrineSavedData.setGraph(data.level, data.graph);
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
