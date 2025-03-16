package xyz.peatral.arcaneforces.content.shrines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModBlockEntities;
import xyz.peatral.arcaneforces.ModTags;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetwork;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkNodeBlockEntity;

import java.util.Optional;
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
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof ShrineNetworkNodeBlockEntity networkLocation) {
            networkLocation.setActive(newState.getValue(ACTIVATED));
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(ModTags.Items.OFFERINGS)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        BlockState above = level.getBlockState(pos.above());
        if (!state.getValue(ACTIVATED) || !above.is(ModTags.Blocks.INCENSE_TRAVEL_STARTERS) || !CandleBlock.isLit(above)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof ShrineNetworkNodeBlockEntity)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        Optional<ShrineNetwork> network = ShrineNetwork.getNetwork(level);
        if (network.isEmpty() || network.get().getNeighbors(pos) == null) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        BlockPos shrine = network.get().getClosestShrine(pos);
        if (shrine.distSqr(player.getOnPos()) < 100) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        if (!player.isCreative()) {
            int candleCount = above.getValue(BlockStateProperties.CANDLES) - 1;
            BlockState updated = candleCount > 0 ? above.setValue(BlockStateProperties.CANDLES, candleCount).setValue(BlockStateProperties.LIT, Boolean.FALSE) : Blocks.AIR.defaultBlockState();
            level.setBlock(pos.above(), updated, Block.UPDATE_ALL);
            stack.setCount(stack.getCount() - 1);
        }

        if (!level.isClientSide()) {
            Vec3 tpPos = Vec3.atCenterOf(shrine).add(new Vec3(0, 0.5, 0));
            player.teleportTo(tpPos.x, tpPos.y, tpPos.z);
        }

        return ItemInteractionResult.CONSUME_PARTIAL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof ShrineNetworkNodeBlockEntity networkLocation) {
            networkLocation.deactivate();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
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
