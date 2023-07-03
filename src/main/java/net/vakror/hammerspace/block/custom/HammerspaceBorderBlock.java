package net.vakror.hammerspace.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HammerspaceBorderBlock extends Block {
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");

    public HammerspaceBorderBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CONNECTED_DOWN, false)
                .setValue(CONNECTED_UP, false)
                .setValue(CONNECTED_NORTH, false)
                .setValue(CONNECTED_SOUTH, false)
                .setValue(CONNECTED_WEST, false)
                .setValue(CONNECTED_EAST, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        return updateBlockState(defaultBlockState(), level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean moved) {
        if (state.getBlock() instanceof HammerspaceBorderBlock) {
            level.setBlock(pos, updateBlockState(state, level, pos), 2);
        }
        super.neighborChanged(state, level, pos, neighbor, neighborPos, moved);
    }

    /**
     * Checks if a specific side of a block can connect to this block.
     *
     * @param world The world to run the check in.
     * @param pos The position of the block to check for.
     * @param side The side of the block to check.
     * @return Whether the side is connectable.
     */
    private boolean isSideConnectable(BlockGetter world, BlockPos pos, Direction side) {
        final BlockState state = world.getBlockState(pos.relative(side));
        return state.getBlock() instanceof HammerspaceBorderBlock;
    }

    public BlockState updateBlockState(BlockState state, BlockGetter level, BlockPos pos) {
        return state.setValue(CONNECTED_DOWN, isSideConnectable(level, pos, Direction.DOWN)).setValue(CONNECTED_EAST, this.isSideConnectable(level, pos, Direction.EAST)).setValue(CONNECTED_NORTH, this.isSideConnectable(level, pos, Direction.NORTH)).setValue(CONNECTED_SOUTH, this.isSideConnectable(level, pos, Direction.SOUTH)).setValue(CONNECTED_UP, this.isSideConnectable(level, pos, Direction.UP)).setValue(CONNECTED_WEST, this.isSideConnectable(level, pos, Direction.WEST));
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
    }
}
