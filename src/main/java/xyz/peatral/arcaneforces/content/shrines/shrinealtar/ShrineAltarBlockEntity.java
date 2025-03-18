package xyz.peatral.arcaneforces.content.shrines.shrinealtar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Triple;
import xyz.peatral.arcaneforces.ModEntities;
import xyz.peatral.arcaneforces.content.shrines.network.*;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo.ShrineHoloEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ShrineAltarBlockEntity extends ShrineNetworkNodeBlockEntity {

    public Set<Triple<BlockPos, Integer, ShrineNetworkLocation>> connectedLocations = Set.of();
    public double furthestDistance;

    private final float OPENING_DURATION = 20;

    private Set<ShrineHoloEntity> holos = new HashSet<>();

    public InteractionResult toggleMap() {
        if (state == State.OPEN) {
            state = State.CLOSING;
            if (!level.isClientSide) {
                cleanupEntities();
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        } else if (state == State.CLOSED) {
            state = State.OPENING;
            if (!level.isClientSide) {
                ShrineHoloEntity ent = ModEntities.SHRINE_HOLO.get().spawn((ServerLevel) level, getBlockPos().above(), MobSpawnType.TRIGGERED);
                holos.add(ent);
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }

    private enum State {
        CLOSED,
        OPENING,
        OPEN,
        CLOSING,
    }
    private State state = State.CLOSED;
    private int ticks = 0;
    private int prevTicks = 0;

    public ShrineAltarBlockEntity(BlockEntityType<ShrineAltarBlockEntity> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        location = new ShrineNetworkLocation("Test shrine");
    }

    private void cleanupEntities() {
        holos.forEach(holo -> holo.setRemoved(Entity.RemovalReason.DISCARDED));
    }

    @Override
    public void setRemoved() {
        cleanupEntities();
        super.setRemoved();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
    }

    public Set<Triple<BlockPos, Integer, ShrineNetworkLocation>> getConnectedLocations() {
        return connectedLocations;
    }

    public float getRenderedShrineOffset(float partialTicks) {
        if (state == State.CLOSED) {
            return 0;
        }
        if (state == State.OPEN) {
            return 1.0f;
        }
        float lerpedTicks = Mth.lerp(partialTicks, prevTicks, ticks);
        float percentage = Mth.clamp(lerpedTicks / OPENING_DURATION, 0, 1.0f);
        return state == State.OPENING ? percentage : 1.0f - percentage;
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();

        saveAdditional(tag, registries);

        state = State.values()[tag.getInt("State")];
        prevTicks = ticks = tag.getInt("Ticks");
        return tag;
    }


    private void tick(Level level, BlockPos pos, BlockState blockState) {
        if (ticks >= OPENING_DURATION) {
            ticks = 0;
            if (state == State.OPENING) {
                state = State.OPEN;
            } else if (state == State.CLOSING) {
                state = State.CLOSED;
            }
        }

        if (state == State.OPENING || state == State.CLOSING) {
            prevTicks = ticks++;
        }

        Optional<ShrineNetwork> optNetwork = ShrineNetwork.getNetwork(level);
        if (optNetwork.isPresent()) {
            ShrineNetwork network = optNetwork.get();
            connectedLocations = network.getShrines(pos);
            furthestDistance = connectedLocations.stream()
                    .map(t -> t.getLeft().distSqr(pos))
                    .max(Double::compareTo)
                    .orElse(Double.NaN);
        }
    }

    // Handle a received update tag here. The default implementation calls #loadAdditional here,
    // so you do not need to override this method if you don't plan to do anything beyond that.
    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);

        tag.putInt("State", state.ordinal());
        tag.putInt("Ticks", ticks);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
        if (t instanceof ShrineAltarBlockEntity ent) {
            ent.tick(level, pos, blockState);
        }
    }
}
