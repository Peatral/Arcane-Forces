package xyz.peatral.arcaneforces.content.shrines.network;

import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class GraphDebugger {
    public static void tick() {
        if (!isActive()) {
            return;
        }

        ShrineNetworkLocationBlockEntity be = getSelectedBE();
        if (be == null)
            return;

        Level level = Minecraft.getInstance().level;
        BlockPos origin = be.getBlockPos();
        VoxelShape shape = level.getBlockState(origin)
                .getBlockSupportShape(level, origin);

        if (!shape.isEmpty()) {
            Outliner.getInstance().chaseAABB("outline", shape.bounds()
                            .move(origin))
                    .lineWidth(1 / 16f)
                    .colored(0xffcc00);
        }

        ClientShrineSavedData.getGraph(level).ifPresent(blockPosGraph -> {
            Optional<ShrineNetworkLocation> location = blockPosGraph.getNodes().stream().filter(loc -> loc.position().equals(origin)).findFirst();
            if (location.isEmpty()) {
                return;
            }

            List<ShrineNetworkLocation> queue = new ArrayList<>();
            queue.add(location.get());

            Set<ShrineNetworkLocation> visited = new HashSet<>();
            while (!queue.isEmpty()) {
                ShrineNetworkLocation pos = queue.removeFirst();
                if (visited.contains(pos)) {
                    continue;
                }
                visited.add(pos);
                Set<ShrineNetworkLocation> neighbors = blockPosGraph.getNeighbors(pos);
                if (neighbors == null) {
                    continue;
                }
                queue.addAll(neighbors);
                for (ShrineNetworkLocation neighbor : neighbors) {
                    Outliner.getInstance().showLine(pos.position().toShortString() + "to" + neighbor.position().toShortString(), pos.position().getCenter(), neighbor.position().getCenter())
                            .lineWidth(1 / 16f);
                }
            }
        });

    }

    public static boolean isActive() {
        return isF3DebugModeActive();
    }

    public static boolean isF3DebugModeActive() {
        return Minecraft.getInstance().getDebugOverlay().showDebugScreen();
    }

    public static ShrineNetworkLocationBlockEntity getSelectedBE() {
        HitResult obj = Minecraft.getInstance().hitResult;
        ClientLevel world = Minecraft.getInstance().level;
        if (obj == null)
            return null;
        if (world == null)
            return null;
        if (!(obj instanceof BlockHitResult ray))
            return null;

        BlockEntity be = world.getBlockEntity(ray.getBlockPos());
        if (be instanceof ShrineNetworkLocationBlockEntity networkLocation)
            return networkLocation;

        return null;
    }
}
