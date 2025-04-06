package ianeli.moredyes.blocks;


import com.mojang.serialization.MapCodec;
import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.entity.ColoredFallingBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.ColorCode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class CustomPowder extends GenericFallingColoredBlock {
    public static Block hardenedState = ModBlocks.CustomConcrete;
    public CustomPowder(Settings settings) {
        super(settings);
    }

    private static boolean hardensIn(BlockState state) {
        return state.getFluidState().isIn(FluidTags.WATER);
    }
    private static boolean hardensOnAnySide(BlockView world, BlockPos pos) {
        boolean bl = false;
        BlockPos.Mutable mutable = pos.mutableCopy();

        for(Direction direction : Direction.values()) {
            BlockState blockState = world.getBlockState(mutable);
            if (direction != Direction.DOWN || hardensIn(blockState)) {
                mutable.set(pos, direction);
                blockState = world.getBlockState(mutable);
                if (hardensIn(blockState) && !blockState.isSideSolidFullSquare(world, pos, direction.getOpposite())) {
                    bl = true;
                    break;
                }
            }
        }

        return bl;
    }
    private static boolean shouldHarden(BlockView world, BlockPos pos, BlockState state) {
        return hardensIn(state) || hardensOnAnySide(world, pos);
    }

    @Override
    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, ColoredFallingBlockEntity fallingBlockEntity) {
        if (shouldHarden(world, pos, currentStateInPos)) {
            world.setBlockState(pos, hardenedState.getDefaultState(), 3);
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = blockView.getBlockState(blockPos);
        return shouldHarden(blockView, blockPos, blockState) ? this.hardenedState.getDefaultState() : super.getPlacementState(ctx);
    }


    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (hardensOnAnySide(world, pos)) {
            //world.setBlockState(pos, hardenedState.getDefaultState(), 3);
            if (world instanceof World w && !w.isClient) {
                int col = 0;
                BlockEntity be = w.getBlockEntity(pos);
                if (be instanceof ColoredBlockEntity cbe) {
                    col = cbe.getColor();
                }
                w.setBlockState(pos, this.hardenedState.getDefaultState());
                BlockEntity be2 = w.getBlockEntity(pos);
                if (be2 instanceof ColoredBlockEntity cbe2) {
                    cbe2.setColor(col);
                }
            }
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return 0xFF0000;
    }
}