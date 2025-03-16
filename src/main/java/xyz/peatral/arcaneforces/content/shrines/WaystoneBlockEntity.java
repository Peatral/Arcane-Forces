package xyz.peatral.arcaneforces.content.shrines;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkNodeBlockEntity;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineSavedData;

import java.util.*;

public class WaystoneBlockEntity extends ShrineNetworkNodeBlockEntity {
    private List<Set<BlockPos>> bells = new ArrayList<>();

    private int timer = 0;

    public WaystoneBlockEntity(BlockEntityType<WaystoneBlockEntity> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }



    public static void tick(Level level, BlockPos pos, BlockState state, WaystoneBlockEntity blockEntity) {

    }

    public void ringBells(Level pLevel, BlockPos pPos, Entity pEntity) {
        bells.clear();
        timer = 5;

        int radius = 2;
        int maxBellsPerShrine = 1;

        if (!level.isClientSide()) {
            ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) level);
            Set<Pair<BlockPos, Integer>> shrines = data.network().getDepths(pPos);
            for (Pair<BlockPos, Integer> shrine : shrines) {
                BlockPos shrinePos = shrine.getFirst();

                WaystoneBlockEntity waystoneBlockEntity = (WaystoneBlockEntity) level.getBlockEntity(shrinePos);
                if (waystoneBlockEntity != null) {
                    addNDistinctPositions(getBells(shrinePos, pLevel, radius), shrine.getSecond(), maxBellsPerShrine);
                }
            }
        }
    }

    public Set<BlockPos> getBells(BlockPos pPos, Level pLevel, int pRadius) {
        Set<BlockPos> bellPositions = new HashSet<>();
        for (int x = -pRadius; x <= pRadius; x++) {
            for (int z = -pRadius; z <= pRadius; z++) {
                for (int y = -pRadius; y <= pRadius; y++) {
                    BlockPos blockPos = pPos.offset(x, y, z);
                    BlockState blockState = pLevel.getBlockState(blockPos);
                    if (blockState.is(Blocks.BELL) && !bellPositions.contains(blockPos)) {
                        bellPositions.add(blockPos);
                    }
                }
            }
        }
        return bellPositions;
    }

    public void addNDistinctPositions(Set<BlockPos> blocks, int depth, int maxAmount) {
        int counter = 0;
        while (bells.size() - 1 < depth) {
            bells.add(new HashSet<>());
        }
        for (BlockPos pos : blocks) {
            if (bells.stream().noneMatch(b -> b.contains(pos)) && bells.get(depth).add(pos)) {
                counter++;
            }
            if (counter >= maxAmount) {
                return;
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
        if (t instanceof WaystoneBlockEntity waystoneBlockEntity) {
            if (waystoneBlockEntity.timer-- > 0 || waystoneBlockEntity.bells.isEmpty()) {
                return;
            }

            Set<BlockPos> bells = waystoneBlockEntity.bells.removeFirst();
            for (BlockPos bellPos : bells) {
                if (!level.getBlockState(bellPos).is(Blocks.BELL)) {
                    continue;
                }
                BellBlock bellBlock = (BellBlock) level.getBlockState(bellPos).getBlock();
                bellBlock.attemptToRing(null, level, bellPos, null);
                for (int i = 0; i < 10; i++) {
                    Vec3 center = bellPos.getCenter().offsetRandom(level.random, 1);
                    level.addParticle(ParticleTypes.DUST_PLUME, center.x, center.y, center.z, 0, 0, 0);
                }
            }

            waystoneBlockEntity.timer = 15;
        }
    }


    public void teleportToClosestShrine(Level pLevel, Player pPlayer) {
        if (level.isClientSide) {
            return;
        }
        ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) pLevel);
        BlockPos closestNode = data.network().getClosestShrine(getBlockPos());
        if (closestNode == null) {
            return;
        }
        Vec3 pos = closestNode.getCenter();
        pPlayer.teleportTo(pos.x, pos.y + 0.5, pos.z);
    }
}
