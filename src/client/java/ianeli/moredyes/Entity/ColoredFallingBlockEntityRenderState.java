package ianeli.moredyes.Entity;

import ianeli.moredyes.entity.ColoredFallingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.EmptyBlockRenderView;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.Nullable;

public class ColoredFallingBlockEntityRenderState extends EntityRenderState implements BlockRenderView {
    public BlockPos fallingBlockPos;
    public BlockPos currentPos;
    public BlockState blockState;
    public BlockRenderView world;
    public int color;

    public ColoredFallingBlockEntityRenderState() {
        this.fallingBlockPos = BlockPos.ORIGIN;
        this.currentPos = BlockPos.ORIGIN;
        this.blockState = Blocks.SAND.getDefaultState();
        this.world = EmptyBlockRenderView.INSTANCE;
    }


    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        return this.world.getBrightness(direction, shaded);
    }

    @Override
    public LightingProvider getLightingProvider() { return this.world.getLightingProvider(); }

    @Override
    public int getColor(BlockPos pos, ColorResolver colorResolver) {
        return 0xFFFFFF;
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) { return null; }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.blockState;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).getFluidState();
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getBottomY() {
        return this.currentPos.getY();
    }
}
