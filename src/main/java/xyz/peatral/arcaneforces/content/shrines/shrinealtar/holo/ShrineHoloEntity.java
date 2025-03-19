package xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import xyz.peatral.arcaneforces.ModTags;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetwork;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.ShrineAltarBlockEntity;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkLocation;

import java.util.Optional;

public class ShrineHoloEntity extends Entity {
    public static final EntityDataAccessor<BlockPos> PARENT = SynchedEntityData.defineId(ShrineHoloEntity.class, EntityDataSerializers.BLOCK_POS);
    public static final EntityDataAccessor<BlockPos> TARGET = SynchedEntityData.defineId(ShrineHoloEntity.class, EntityDataSerializers.BLOCK_POS);

    public ShrineHoloEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void setup(BlockPos parent, BlockPos target) {
        this.getEntityData().set(PARENT, parent);
        this.getEntityData().set(TARGET, target);
    }

    public Optional<ShrineNetworkLocation> getTarget() {
        return ShrineNetwork.getNetwork(level()).map(n -> n.getLocation(getEntityData().get(TARGET)));
    }

    public Optional<ShrineAltarBlockEntity> getParent() {
        BlockEntity be = this.level().getBlockEntity(getEntityData().get(ShrineHoloEntity.PARENT));
        return be instanceof ShrineAltarBlockEntity parent ? Optional.of(parent) : Optional.empty();
    }

    private boolean isValid() {
        Optional<ShrineAltarBlockEntity> parent = getParent();
        Optional<ShrineNetworkLocation> target = getTarget();

        return parent.isPresent() && target.isPresent()
                && parent.get().getState() != ShrineAltarBlockEntity.State.CLOSED
                && parent.get().isLocationConnected(getEntityData().get(TARGET));
    }

    public float getAnimationLerp(float partialTicks) {
        return isValid() ? getParent().get().getHoloAnimationProgress(partialTicks) : 0.0f;
    }

    @Override
    public boolean isPickable() {
        return isValid() && getParent().get().getState() == ShrineAltarBlockEntity.State.OPEN;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        return interact(player, hand);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ModTags.Items.OFFERINGS)) {
            if (!player.isCreative()) {
                stack.setCount(stack.getCount() - 1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void tick() {
        super.tick();

        if (!isValid()) {
            //setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PARENT, BlockPos.ZERO).define(TARGET, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.getEntityData().set(PARENT, NbtUtils.readBlockPos(compound, "parent").get());
        this.getEntityData().set(TARGET, NbtUtils.readBlockPos(compound, "target").get());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("parent", NbtUtils.writeBlockPos(this.getEntityData().get(PARENT)));
        compound.put("target", NbtUtils.writeBlockPos(this.getEntityData().get(TARGET)));
    }
}
