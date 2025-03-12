package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientShrineSavedData {
    private static Map<ResourceKey<Level>, Graph<BlockPos>> graphs = new HashMap<>();

    public static Graph<BlockPos> setGraph(ResourceKey<Level> level, Graph<BlockPos> graph) {
        return ClientShrineSavedData.graphs.put(level, graph);
    }

    public static Graph<BlockPos> unloadGraph(Level level) {
        return graphs.remove(level.dimension());
    }

    public static Optional<Graph<BlockPos>> getGraph(Level level) {
        Graph<BlockPos> graph = graphs.get(level.dimension());
        if (graph == null) {
            return Optional.empty();
        }
        return Optional.of(graph);
    }

}
