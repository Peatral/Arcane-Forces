package xyz.peatral.arcaneforces.content.shrines;

import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphDebugger {
    public static void tick() {
        if (!isActive()) {
            return;
        }

        BellRingerBlockEntity be = getSelectedBE();
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
            List<BlockPos> queue = new ArrayList<>();
            queue.add(origin);
            Set<BlockPos> visited = new HashSet<>();
            while (!queue.isEmpty()) {
                BlockPos pos = queue.removeFirst();
                if (visited.contains(pos)) {
                    continue;
                }
                visited.add(pos);
                Set<BlockPos> neighbors = blockPosGraph.getNeighbors(pos);
                if (neighbors == null) {
                    continue;
                }
                queue.addAll(neighbors);
                for (BlockPos neighbor : neighbors) {
                    Outliner.getInstance().showLine(pos.toShortString() + "to" + neighbor.toShortString(), pos.getCenter(), neighbor.getCenter())
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

    public static BellRingerBlockEntity getSelectedBE() {
        HitResult obj = Minecraft.getInstance().hitResult;
        ClientLevel world = Minecraft.getInstance().level;
        if (obj == null)
            return null;
        if (world == null)
            return null;
        if (!(obj instanceof BlockHitResult ray))
            return null;

        BlockEntity be = world.getBlockEntity(ray.getBlockPos());
        if (be instanceof BellRingerBlockEntity ringer)
            return ringer;

        return null;
    }
}
