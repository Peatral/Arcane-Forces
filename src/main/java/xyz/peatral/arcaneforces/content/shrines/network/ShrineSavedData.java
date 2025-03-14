package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.peatral.arcaneforces.Main;

import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShrineSavedData extends SavedData {
    private static final BiFunction<ShrineNetworkLocation, ShrineNetworkLocation, Double> NODE_DISTANCE_FUNCTION = (a, b) -> a.position().getCenter().distanceTo(b.position().getCenter());

    // TODO: While this is shorter and less janky than the regular codec, this probably wastes bandwidth by storing each entry multiple times (as key and each time its a neighbor)
    public static final StreamCodec<RegistryFriendlyByteBuf, Graph<ShrineNetworkLocation>> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ShrineNetworkLocation.STREAM_CODEC, ByteBufCodecs.collection(HashSet::new, ShrineNetworkLocation.STREAM_CODEC)), Graph::getAdjacency,
            (locationMap) -> new Graph<>(locationMap, NODE_DISTANCE_FUNCTION)
    );


    // TODO: This feels pretty janky, if the key order of the map that the graph uses isn't stable, this will break
    // The locations stored is a lut for the actual values
    // The adjacency is a list where each entry corresponds to the location with the same index in the lut
    // Each value in an adjacency list is also an index to the corresponding location in the lut
    public static final Codec<Graph<ShrineNetworkLocation>> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ShrineNetworkLocation.CODEC.listOf()
                            .fieldOf("locations")
                            .forGetter(g -> g.getNodes().stream().toList()),
                    Codec.INT.listOf().listOf()
                            .fieldOf("adjacency")
                            .forGetter(g -> {
                                List<ShrineNetworkLocation> locs = g.getNodes().stream().toList();
                                List<Set<ShrineNetworkLocation>> idcs = g.getAdjacency().values().stream().toList();
                                return IntStream.range(0, locs.size()).mapToObj(i -> idcs.get(i).stream().map(locs::indexOf).toList()).toList();
                            })
            ).apply(instance, (locations, adjacency) -> {
                Map<ShrineNetworkLocation, Set<ShrineNetworkLocation>> map = new HashMap<>();
                for (int i = 0; i < locations.size(); i++) {
                    ShrineNetworkLocation loc = locations.get(i);
                    Set<ShrineNetworkLocation> adj = adjacency.get(i).stream().map(locations::get).collect(Collectors.toCollection(HashSet::new));
                    map.put(loc, adj);
                }
                return new Graph<>(map, NODE_DISTANCE_FUNCTION);
            })
    );


    private HashMap<BlockPos, ShrineNetworkLocation> posToLocation = new HashMap<>();
    private Graph<ShrineNetworkLocation> graph = new Graph<>(NODE_DISTANCE_FUNCTION);

    private ServerLevel level;

    public ShrineSavedData(ServerLevel level) {
        this.level = level;
    }

    public boolean addLocation(ShrineNetworkLocation location) {
        boolean modified = graph.addAndUpdateNeighbors(location, (a, b) -> a.position().getCenter().distanceTo(b.position().getCenter()) <= 10);
        if (modified) {
            posToLocation.put(location.position(), location);
            setDirty();
        }
        return modified;
    }

    public boolean removeLocation(BlockPos pos) {
        boolean modified = graph.remove(getLocation(pos));
        if (modified) {
            posToLocation.remove(pos);
            setDirty();
        }
        return modified;
    }

    public Set<ShrineNetworkLocation> getShrines() {
        return graph.getNodes();
    }

    public Graph<ShrineNetworkLocation> getGraph() {
        return graph;
    }

    public Set<Pair<ShrineNetworkLocation, Integer>> getDepths(BlockPos pos) {
        return graph.getDepths(getLocation(pos));
    }

    public ShrineNetworkLocation getLocation(BlockPos pos) {
        return posToLocation.get(pos);
    }


    @Override
    public void setDirty() {
        super.setDirty();
        sync();
    }

    private void sync() {
        PacketDistributor.sendToPlayersInDimension(level, new SyncGraphPayload(level.dimension(), graph));
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        DataResult<Tag> res = CODEC.encodeStart(NbtOps.INSTANCE, graph);
        if (res.error().isPresent()) {
            return pTag;
        }
        pTag.put("graph", res.getOrThrow());
        return pTag;
    }

    private static ShrineSavedData create(ServerLevel level) {
        return new ShrineSavedData(level);
    }

    private static ShrineSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider, ServerLevel level) {
        ShrineSavedData data = create(level);
        if (!tag.contains("graph")) {
            return data;
        }
        DataResult<Pair<Graph<ShrineNetworkLocation>, Tag>> res = CODEC.decode(NbtOps.INSTANCE, tag.get("graph"));
        if (res.error().isPresent()) {
            return data;
        }
        data.graph = res.getOrThrow().getFirst();
        return data;
    }

    public static ShrineSavedData computeIfAbsent(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(() -> ShrineSavedData.create(level), (CompoundTag tag, HolderLookup.Provider lookupProvider) -> ShrineSavedData.load(tag, lookupProvider, level)), "shrines");
    }

    public static void syncToPlayer(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        PacketDistributor.sendToPlayer(player, new SyncGraphPayload(level.dimension(), ShrineSavedData.computeIfAbsent(level).getGraph()));
    }
}
