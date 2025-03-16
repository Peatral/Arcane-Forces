package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;

public class ShrineSavedData extends SavedData implements ShrineNetwork.OnChangeListener {
    private ServerLevel level;
    private ShrineNetwork network;

    public ShrineSavedData(ServerLevel level) {
        this.level = level;
        this.network = new ShrineNetwork();
        this.network.addChangeListener(this);
    }


    @Override
    public void onNetworkChange() {
        setDirty();
        sync();
    }

    private void sync() {
        PacketDistributor.sendToPlayersInDimension(level, new SyncNetworkPayload(level.dimension(), network));
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        DataResult<Tag> res = ShrineNetwork.CODEC.encodeStart(NbtOps.INSTANCE, network);
        if (res.error().isPresent()) {
            return pTag;
        }
        pTag.put("network", res.getOrThrow());
        return pTag;
    }

    private static ShrineSavedData create(ServerLevel level) {
        return new ShrineSavedData(level);
    }

    private static ShrineSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider, ServerLevel level) {
        ShrineSavedData data = create(level);
        if (!tag.contains("network")) {
            return data;
        }
        DataResult<Pair<ShrineNetwork, Tag>> res = ShrineNetwork.CODEC.decode(NbtOps.INSTANCE, tag.get("network"));
        if (res.error().isPresent()) {
            return data;
        }
        data.network = res.getOrThrow().getFirst();
        data.network.addChangeListener(data);
        return data;
    }

    public static ShrineSavedData computeIfAbsent(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(() -> ShrineSavedData.create(level), (CompoundTag tag, HolderLookup.Provider lookupProvider) -> ShrineSavedData.load(tag, lookupProvider, level)), "shrines");
    }

    public static void syncToPlayer(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        PacketDistributor.sendToPlayer(player, new SyncNetworkPayload(level.dimension(), ShrineSavedData.computeIfAbsent(level).network()));
    }

    public ShrineNetwork network() {
        return this.network;
    }
}
