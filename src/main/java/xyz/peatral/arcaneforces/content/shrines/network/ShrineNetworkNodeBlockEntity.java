package xyz.peatral.arcaneforces.content.shrines.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class ShrineNetworkNodeBlockEntity extends BlockEntity {
    protected ShrineNetworkLocation location;

    public ShrineNetworkNodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void setActive(boolean active) {
        if (level.isClientSide()) return;
        ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) level);
        if (active) {
            data.network().addNode(getBlockPos(), Optional.ofNullable(location));
        } else {
            data.network().removeNode(getBlockPos());
        }
    }

    public void activate() {
        setActive(true);
    }

    public void deactivate() {
        setActive(false);
    }

    public ShrineNetworkLocation getLocation() {
        return location;
    }

    public void setLocation(ShrineNetworkLocation location) {
        this.location = location;
    }
}
