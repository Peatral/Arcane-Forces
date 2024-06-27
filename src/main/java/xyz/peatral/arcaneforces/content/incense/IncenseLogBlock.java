package xyz.peatral.arcaneforces.content.incense;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class IncenseLogBlock extends RotatedPillarBlock {
    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
    public static final BooleanProperty PLACED_BY_PLAYER = BooleanProperty.create("placed_by_player");
    public IncenseLogBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
        registerDefaultState(
                super.defaultBlockState()
                        .setValue(STATE, State.YOUNG)
                        .setValue(PLACED_BY_PLAYER, false)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(STATE, State.AGED).setValue(PLACED_BY_PLAYER, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(STATE).add(PLACED_BY_PLAYER));
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        // TODO: Balancing: tweak these numbers
        if (pState.getValue(STATE).equals(State.SCARRED) && pRandom.nextInt(3) == 0) {
            pLevel.setBlock(pPos, pState.setValue(STATE, State.RESIN), 2);
        }
        if (pState.getValue(STATE).equals(State.YOUNG) && pRandom.nextInt(3) == 0) {
            pLevel.setBlock(pPos, pState.setValue(STATE, State.AGED), 2);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(PLACED_BY_PLAYER) && (
                pState.getValue(STATE).equals(State.YOUNG) || pState.getValue(STATE).equals(State.SCARRED)
        ) || super.isRandomlyTicking(pState);
    }

    public enum State implements StringRepresentable {
        YOUNG("young"),
        AGED("aged"),
        SCARRED("scarred"),
        RESIN("resin");

        private final String serializedName;

        State(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serializedName;
        }
    }
}
