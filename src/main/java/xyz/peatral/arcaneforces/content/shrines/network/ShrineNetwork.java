package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShrineNetwork {
    private static final double NEIGHBOR_DISTANCE = 10.0d;
    private static final BiFunction<BlockPos, BlockPos, Double> DISTANCE_FUNCTION = (a, b) -> a.getCenter().distanceTo(b.getCenter());

    public static final StreamCodec<RegistryFriendlyByteBuf, ShrineNetwork> STREAM_CODEC = new ShrineNetworkStreamCodec();;

    // TODO: the nodes are sorted 3 times
    public static final Codec<ShrineNetwork> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.listOf()
                            .fieldOf("positions")
                            .forGetter(n -> n.graph.getNodes().stream().sorted().toList()),
                    Codec.INT.listOf().listOf()
                            .fieldOf("adjacency")
                            .forGetter(n -> {
                                List<BlockPos> locs = n.graph.getNodes().stream().sorted().toList();
                                return locs.stream().map(loc -> n.graph.getAdjacency()
                                        .get(loc)
                                        .stream()
                                        .map(locs::indexOf)
                                        .toList()
                                ).toList();
                            }),
                    Codec.unboundedMap(Codec.STRING, ShrineNetworkLocation.CODEC)
                            .fieldOf("locations")
                            .forGetter(n -> {
                                    List<BlockPos> locs = n.graph.getNodes().stream().sorted().toList();
                                    return n.locations.entrySet().stream().collect(Collectors.toMap(
                                            e -> String.valueOf(locs.indexOf(e.getKey())),
                                            Map.Entry::getValue
                                    ));
                            })
            ).apply(instance, (positions, adjacency, locations) -> {
                Map<BlockPos, Set<BlockPos>> positionMap = IntStream.range(0, positions.size())
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(
                                positions.get(i),
                                adjacency.get(i).stream().map(positions::get).collect(Collectors.toCollection(HashSet::new))
                        )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                Map<BlockPos, ShrineNetworkLocation> locationMap = locations.entrySet().stream().collect(Collectors.toMap(
                        e -> positions.get(Integer.parseInt(e.getKey())),
                        Map.Entry::getValue));
                Graph<BlockPos> graph = new Graph<>(positionMap, DISTANCE_FUNCTION);
                return new ShrineNetwork(locationMap, graph);
            })
    );

    private Map<BlockPos, ShrineNetworkLocation> locations = new HashMap<>();
    private Graph<BlockPos> graph = new Graph<>(DISTANCE_FUNCTION);
    private final Set<OnChangeListener> changeListeners = new HashSet<>();

    public ShrineNetwork() {
    }

    public ShrineNetwork(Map<BlockPos, ShrineNetworkLocation> locations, Graph<BlockPos> graph) {
        this.locations = locations;
        this.graph = graph;
    }

    public ShrineNetworkLocation getLocation(BlockPos pos) {
        return locations.get(pos);
    }

    public boolean addNode(BlockPos pos, Optional<ShrineNetworkLocation> location) {
        location.ifPresent(loc -> locations.put(pos, loc));
        return addNode(pos);
    }

    public boolean addNode(BlockPos pos) {
        boolean modified = graph.upsertNeighbors(pos, (pos1, pos2) ->
                DISTANCE_FUNCTION.apply(pos1, pos2) < NEIGHBOR_DISTANCE
        );
        if (modified) {
            triggerUpdate();
        }
        return modified;
    }

    public void removeNode(BlockPos pos) {
        locations.remove(pos);
        boolean modified = graph.remove(pos);
        if (modified) {
            triggerUpdate();
        }
    }

    public Set<BlockPos> getNeighbors(BlockPos pos) {
        return graph.getNeighbors(pos);
    }

    public Set<BlockPos> getPositions() {
        return graph.getNodes();
    }

    public Set<Pair<BlockPos, Integer>> getDepths(BlockPos pos) {
        return graph.getDepths(pos);
    }

    public BlockPos getClosestShrine(BlockPos from) {
        return graph.getClosestNode(from, pos -> locations.containsKey(pos));
    }

    private void triggerUpdate() {
        changeListeners.forEach(OnChangeListener::onNetworkChange);
    }

    public void addChangeListener(OnChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(OnChangeListener listener) {
        changeListeners.remove(listener);
    }

    public interface OnChangeListener {
        void onNetworkChange();
    }

    public static Optional<ShrineNetwork> getNetwork(Level level) {
        if (level.isClientSide) {
            return ClientShrineSavedData.getNetwork(level);
        } else {
            return Optional.of(ShrineSavedData.computeIfAbsent((ServerLevel) level).network());
        }
    }

    private static class ShrineNetworkStreamCodec implements StreamCodec<RegistryFriendlyByteBuf, ShrineNetwork> {
        @Override
        public ShrineNetwork decode(RegistryFriendlyByteBuf buffer) {
            int nodeCount = buffer.readInt();
            List<BlockPos> positions = new ArrayList<>(nodeCount);
            for (int i = 0; i < nodeCount; ++i) {
                positions.add(buffer.readBlockPos());
            }
            Map<BlockPos, Set<BlockPos>> adjacency = new HashMap<>(nodeCount);
            for (int i = 0; i < nodeCount; ++i) {
                int neighborCount = buffer.readInt();
                Set<BlockPos> neighbors = new HashSet<>(neighborCount);
                for (int j = 0; j < neighborCount; ++j) {
                    neighbors.add(positions.get(buffer.readInt()));
                }
                adjacency.put(positions.get(i), neighbors);
            }
            Graph<BlockPos> graph = new Graph<>(adjacency, ShrineNetwork.DISTANCE_FUNCTION);
            Map<BlockPos, ShrineNetworkLocation> locations = new HashMap<>(nodeCount);
            int locationCount = buffer.readInt();
            for (int i = 0; i < locationCount; ++i) {
                locations.put(
                        positions.get(buffer.readInt()),
                        ShrineNetworkLocation.STREAM_CODEC.decode(buffer)
                );
            }
            return new ShrineNetwork(locations, graph);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, ShrineNetwork value) {
            List<BlockPos> positions = new ArrayList<>(value.graph.getNodes());
            buffer.writeInt(positions.size());
            positions.forEach(buffer::writeBlockPos);
            for (BlockPos pos : positions) {
                Set<BlockPos> neighbors = value.graph.getNeighbors(pos);
                buffer.writeInt(neighbors.size());
                neighbors.stream().map(positions::indexOf).forEach(buffer::writeInt);
            }
            buffer.writeInt(value.locations.size());
            for (Map.Entry<BlockPos, ShrineNetworkLocation> location : value.locations.entrySet()) {
                int idx = positions.indexOf(location.getKey());
                buffer.writeInt(idx);
                ShrineNetworkLocation.STREAM_CODEC.encode(buffer, location.getValue());
            }
        }
    }
}
