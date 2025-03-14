package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModBlockEntities;

import java.util.stream.Stream;

public class WaystoneBlock extends Block implements EntityBlock {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public WaystoneBlock(BlockBehaviour.Properties properties) {
        super(properties
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
        registerDefaultState(
                super.defaultBlockState().setValue(ACTIVATED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ACTIVATED));
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        if (!level.isClientSide()) {
            ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) level);
            if (newState.getValue(ACTIVATED)) {
                data.addShrine(pos);
            } else {
                data.removeShrine(pos);
            }
        }
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        super.destroy(pLevel, pPos, pState);
        if (!pLevel.isClientSide() && pState.getValue(ACTIVATED)) {
            ShrineSavedData data = ShrineSavedData.computeIfAbsent((ServerLevel) pLevel);
            data.removeShrine(pPos);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.WAYSTONE.get() ? WaystoneBlockEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WaystoneBlockEntity(ModBlockEntities.WAYSTONE.get(), pPos, pState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pState.getValue(ACTIVATED)) {
            return InteractionResult.PASS;
        }

        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof WaystoneBlockEntity waystoneBlockEntity) {
            waystoneBlockEntity.ringBells(pLevel, pPos, pPlayer);
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Stream.of(
                Block.box(2, 0, 2, 14, 2, 14),
                Block.box(3, 2, 3, 13, 4, 13),
                Block.box(4, 4, 4, 12, 16, 12)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return false;
    }


}
