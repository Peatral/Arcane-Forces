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
    private static Map<ResourceKey<Level>, ShrineNetwork> networks = new HashMap<>();

    public static ShrineNetwork setNetwork(ResourceKey<Level> level, ShrineNetwork graph) {
        return ClientShrineSavedData.networks.put(level, graph);
    }

    public static ShrineNetwork unloadNetwork(Level level) {
        return networks.remove(level.dimension());
    }

    public static Optional<ShrineNetwork> getNetwork(Level level) {
        ShrineNetwork graph = networks.get(level.dimension());
        if (graph == null) {
            return Optional.empty();
        }
        return Optional.of(graph);
    }

}
