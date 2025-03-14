package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkLocation;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkLocationBlockEntity;

public class ShrineAltarBlockEntity extends ShrineNetworkLocationBlockEntity {

    public ShrineAltarBlockEntity(BlockEntityType<ShrineAltarBlockEntity> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        location = new ShrineNetworkLocation("Test shrine", pPos, true);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShrineAltarBlockEntity blockEntity) {

    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
    }
}
