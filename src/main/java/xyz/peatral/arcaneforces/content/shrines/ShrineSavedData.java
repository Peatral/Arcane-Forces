package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.peatral.arcaneforces.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ShrineSavedData extends SavedData {
    private Graph<BlockPos> graph = new Graph<>();

    private ServerLevel level;

    public ShrineSavedData(ServerLevel level) {
        this.level = level;
    }

    public boolean addShrine(BlockPos pos) {
        boolean modified = graph.addAndUpdateNeighbors(pos, (pos1, pos2) -> pos1.getCenter().distanceTo(pos2.getCenter()) <= 10);
        if (modified) {
            setDirty();
        }
        return modified;
    }

    public boolean removeShrine(BlockPos pos) {
        boolean modified = graph.remove(pos);
        if (modified) {
            setDirty();
        }
        return modified;
    }

    public Set<BlockPos> getShrines() {
        return graph.getAllNodes();
    }

    public Graph<BlockPos> getGraph() {
        return graph;
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
        ListTag list = new ListTag();
        List<BlockPos> shrines = new ArrayList<>(graph.getAllNodes());
        for (BlockPos blockPos : shrines) {
            CompoundTag entry = new CompoundTag();
            entry.put("pos", NbtUtils.writeBlockPos(blockPos));
            list.add(entry);
        }
        pTag.put("shrines", list);
        ListTag adjacency = new ListTag();
        for (BlockPos shrine : shrines) {
            Set<BlockPos> neighbors = graph.getNeighbors(shrine);
            if (neighbors == null) {
                continue;
            }
            List<Integer> neighborIndices = neighbors.stream().map(shrines::indexOf).toList();
            adjacency.add(new IntArrayTag(neighborIndices));
        }
        pTag.put("adjacency", adjacency);
        return pTag;
    }

    private static ShrineSavedData create(ServerLevel level) {
        return new ShrineSavedData(level);
    }

    private static ShrineSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider, ServerLevel level) {
        Logger logger = Logger.getLogger(Main.MOD_ID);

        ShrineSavedData data = create(level);

        ListTag shrineTag = tag.getList("shrines", Tag.TAG_COMPOUND);
        ListTag adjacencyTag = tag.getList("adjacency", Tag.TAG_INT_ARRAY);

        List<BlockPos> shrines = new ArrayList<>();
        for (int i = 0; i < shrineTag.size(); i++) {
            CompoundTag entry = shrineTag.getCompound(i);
            BlockPos pos = NbtUtils.readBlockPos(entry, "pos").orElse(null);
            shrines.add(pos);
            if (pos != null) {
                data.graph.addNode(pos);
            }
        }

        if (shrineTag.size() != adjacencyTag.size()) {
            logger.info(String.format("Could not parse shrine graph! Only loaded vertices! (%d vertices, %d adjacency lists)", shrineTag.size(), adjacencyTag.size()));
            return data;
        }

        for (int i = 0; i < shrines.size(); i++) {
            BlockPos shrine = shrines.get(i);
            if (shrine == null) {
                continue;
            }
            int[] neighborsIndices = adjacencyTag.getIntArray(i);
            Set<BlockPos> neighbors = Arrays.stream(neighborsIndices)
                    .mapToObj(shrines::get)
                    .collect(Collectors.toSet());
            data.graph.setNeighbors(shrine, neighbors);
        }

        logger.info(String.format("Succesfully loaded shrine graph with %d vertices!", shrines.size()));

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
