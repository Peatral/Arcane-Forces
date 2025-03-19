package xyz.peatral.arcaneforces.content.shrines.shrinealtar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;
import xyz.peatral.arcaneforces.ModEntities;
import xyz.peatral.arcaneforces.content.shrines.network.*;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo.ShrineHoloEntity;

import java.util.*;

public class ShrineAltarBlockEntity extends ShrineNetworkNodeBlockEntity {

    public enum State {
        CLOSED,
        OPENING,
        OPEN,
        CLOSING,
    }

    private final float OPENING_DURATION = 15;

    private Set<Triple<BlockPos, Integer, ShrineNetworkLocation>> connectedLocations = Set.of();
    private double furthestDistance;

    private Map<BlockPos, UUID> holoUUIDs = new HashMap<>();

    private State state = State.CLOSED;
    private int ticks = 0;
    private int prevTicks = 0;



    public ShrineAltarBlockEntity(BlockEntityType<ShrineAltarBlockEntity> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        location = new ShrineNetworkLocation("Test shrine");
    }

    public InteractionResult toggleMap() {
        if (state == State.OPEN) {
            state = State.CLOSING;
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        } else if (state == State.CLOSED) {
            state = State.OPENING;
            if (!level.isClientSide) {
                connectedLocations.forEach(location -> {
                    spawnHolo(location.getLeft()).ifPresent(holo -> holoUUIDs.put(location.getLeft(), holo.getUUID()));
                });
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }

    private Optional<ShrineHoloEntity> getHolo(UUID uuid) {
        if (uuid == null || level.isClientSide) {
            return Optional.empty();
        }
        Entity ent = ((ServerLevel) level).getEntities().get(uuid);
        return ent instanceof ShrineHoloEntity holo ? Optional.of(holo) : Optional.empty();
    }

    public void removeAllHolos() {
        holoUUIDs.forEach((key, value) -> getHolo(value)
                .ifPresent(h -> h.setRemoved(Entity.RemovalReason.DISCARDED))
        );
        holoUUIDs.clear();
    }

    public Optional<Vec3> getHoloPosition(BlockPos target) {
        Vec3 center = getBlockPos().getCenter();
        return isLocationConnected(target)
                ? Optional.of(target.getCenter()
                    .subtract(center)
                    .scale(3 / Math.sqrt(furthestDistance))
                    .add(center).add(0, 1, 0)
                ) : Optional.empty();
    }

    public boolean isLocationConnected(BlockPos location) {
        return connectedLocations.stream().anyMatch(t -> t.getLeft().equals(location));
    }

    public State getState() {
        return state;
    }

    public float getHoloAnimationProgress(float partialTicks) {
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


    private void tick(Level level, BlockPos pos, BlockState blockState) {
        if (ticks >= OPENING_DURATION) {
            ticks = 0;
            if (state == State.OPENING) {
                state = State.OPEN;
            } else if (state == State.CLOSING) {
                state = State.CLOSED;
            }
            setChanged();
        }

        if (state == State.OPENING || state == State.CLOSING) {
            prevTicks = ticks++;
            setChanged();
        }

        updateHolos(level, pos);
    }

    private void updateHolos(Level level, BlockPos pos) {
        Optional<ShrineNetwork> optNetwork = ShrineNetwork.getNetwork(level);
        if (optNetwork.isPresent()) {
            ShrineNetwork network = optNetwork.get();
            connectedLocations = network.getShrines(pos);
            furthestDistance = connectedLocations.stream()
                    .map(t -> t.getLeft().distSqr(pos))
                    .max(Double::compareTo)
                    .orElse(Double.NaN);
            if (!level.isClientSide) {
                if (state != State.CLOSED) {
                    spawnMissingHolos();
                } else {
                    removeAllHolos();
                }
                cleanupInvalidHolos();
                setChanged();
            }
        }
    }

    private void spawnMissingHolos() {
        connectedLocations.forEach(location -> getHoloPosition(location.getLeft())
                .ifPresent(p -> {
                    Optional<ShrineHoloEntity> ent;
                    BlockPos holoTarget = location.getLeft();
                    if (!holoUUIDs.containsKey(holoTarget)) {
                        ent = spawnHolo(holoTarget);
                    } else {
                        ent = getHolo(holoUUIDs.get(holoTarget));
                    }
                    ent.ifPresent(holo -> {
                        holo.setPos(p);
                        holoUUIDs.put(holoTarget, holo.getUUID());
                        setChanged();
                    });
                })
        );
    }

    private void cleanupInvalidHolos() {
        Set<BlockPos> keysForRemoval = new HashSet<>();
        holoUUIDs.entrySet().stream()
                .filter(e ->
                        connectedLocations.stream().noneMatch(t -> t.getLeft().equals(e.getKey()))
                                || getHolo(e.getValue()).isEmpty()
                )
                .forEach(e -> {
                    getHolo(e.getValue()).ifPresent(holo -> {
                        holo.remove(Entity.RemovalReason.DISCARDED);
                    });
                    keysForRemoval.add(e.getKey());
                    setChanged();
                });
        keysForRemoval.forEach(holoUUIDs::remove);
    }

    private Optional<ShrineHoloEntity> spawnHolo(BlockPos target) {
        if (level.isClientSide) {
            return Optional.empty();
        }
        return Optional.ofNullable(ModEntities.SHRINE_HOLO.get().spawn(
                (ServerLevel) level,
                (t) -> {
                    t.setPos(getHoloPosition(target).orElse(target.getCenter()));
                    t.setup(getBlockPos(), target);
                },
                getBlockPos().above(),
                MobSpawnType.TRIGGERED,
                false,
                false
        ));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        state = State.values()[tag.getInt("State")];
        prevTicks = ticks = tag.getInt("Ticks");
        ListTag holoTags = tag.getList("HoloTags", ListTag.TAG_COMPOUND);
        holoUUIDs = new HashMap<>(holoTags.size());
        holoTags.forEach(t -> {
            CompoundTag holoTag = (CompoundTag) t;
            BlockPos target = NbtUtils.readBlockPos(holoTag, "Target").get();
            UUID uuid = holoTag.getUUID("UUID");
            holoUUIDs.put(target, uuid);
        });
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("State", state.ordinal());
        tag.putInt("Ticks", ticks);
        ListTag holoTags = new ListTag();
        holoUUIDs.entrySet().stream().forEach(e -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("UUID", e.getValue());
            entry.put("Target", NbtUtils.writeBlockPos(e.getKey()));
            holoTags.add(entry);
        });
        tag.put("HoloTags", holoTags);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
        if (t instanceof ShrineAltarBlockEntity ent) {
            ent.tick(level, pos, blockState);
        }
    }
}
