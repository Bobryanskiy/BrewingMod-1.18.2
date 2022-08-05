package net.bobr.testmod.block.custom;

import net.bobr.testmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class MeadBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty AMOUNT = Properties.PICKLES;
    protected static final VoxelShape ONE_BEER_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);
    protected static final VoxelShape TWO_BEERS_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);
    protected static final VoxelShape THREE_BEERS_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);
    protected static final VoxelShape FOUR_BEERS_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);

    public MeadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AMOUNT, 1));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(AMOUNT, Math.min(4, blockState.get(AMOUNT) + 1)).with(FACING, blockState.get(FACING));
        }
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().rotateYClockwise());
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.MEAD);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(AMOUNT)) {
            default: {
                return ONE_BEER_SHAPE;
            }
            case 2: {
                return TWO_BEERS_SHAPE;
            }
            case 3: {
                return THREE_BEERS_SHAPE;
            }
            case 4:
                return FOUR_BEERS_SHAPE;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, FACING);
    }

    @Override
    public boolean canPlaceAt(BlockState blockState, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState floor = world.getBlockState(blockPos);
        return !floor.getCollisionShape(world, blockPos).getFace(Direction.UP).isEmpty() || floor.isSideSolidFullSquare(world, blockPos, Direction.UP);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(AMOUNT) < 4) {
            return true;
        }
        return super.canReplace(state, context);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
