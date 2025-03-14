package xyz.peatral.arcaneforces.content.shrines.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientShrineSavedData {
    private static Map<ResourceKey<Level>, Graph<ShrineNetworkLocation>> graphs = new HashMap<>();

    public static Graph<ShrineNetworkLocation> setGraph(ResourceKey<Level> level, Graph<ShrineNetworkLocation> graph) {
        return ClientShrineSavedData.graphs.put(level, graph);
    }

    public static Graph<ShrineNetworkLocation> unloadGraph(Level level) {
        return graphs.remove(level.dimension());
    }

    public static Optional<Graph<ShrineNetworkLocation>> getGraph(Level level) {
        Graph<ShrineNetworkLocation> graph = graphs.get(level.dimension());
        if (graph == null) {
            return Optional.empty();
        }
        return Optional.of(graph);
    }

}
