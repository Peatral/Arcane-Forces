package xyz.peatral.arcaneforces.content.shrines.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ShrineNetworkLocationBlockEntity extends BlockEntity {
    protected ShrineNetworkLocation location;

    public ShrineNetworkLocationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public boolean setActive(boolean active) {
        if (level.isClientSide() || location == null) return false;
        ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) level);
        if (active) {
            return data.addLocation(location);
        } else {
            return data.removeLocation(getBlockPos());
        }
    }

    public boolean activate() {
        return setActive(true);
    }

    public boolean deactivate() {
        return setActive(false);
    }

    public ShrineNetworkLocation getLocation() {
        return location;
    }

    public void setLocation(ShrineNetworkLocation location) {
        this.location = location;
    }
}
