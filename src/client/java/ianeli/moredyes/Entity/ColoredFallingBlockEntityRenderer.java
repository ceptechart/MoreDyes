package ianeli.moredyes.Entity;

import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.entity.ColoredFallingBlockEntity;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.LoadedBlockEntityModels;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.FallingBlockEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.List;

public class ColoredFallingBlockEntityRenderer extends EntityRenderer<ColoredFallingBlockEntity, ColoredFallingBlockEntityRenderState> {
    private final BlockRenderManager blockRenderManager;

    public ColoredFallingBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    public boolean shouldRender(ColoredFallingBlockEntity fallingBlockEntity, Frustum frustum, double d, double e, double f) {
        if (!super.shouldRender(fallingBlockEntity, frustum, d, e, f)) {
            return false;
        } else {
            return fallingBlockEntity.getBlockState() != fallingBlockEntity.getWorld().getBlockState(fallingBlockEntity.getBlockPos());
        }
    }


    /*public void render(ColoredFallingBlockEntityRenderState fallingBlockEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BlockState blockState = Blocks.REDSTONE_BLOCK.getDefaultState();
        if (blockState.getRenderType() == BlockRenderType.MODEL) {
            matrixStack.push();
            matrixStack.translate((double)-0.5F, (double)0.0F, (double)-0.5F);
            int light = LightmapTextureManager.pack(15, 15);

            List<BlockModelPart> list = this.blockRenderManager.getModel(blockState).getParts(Random.create(blockState.getRenderingSeed(fallingBlockEntityRenderState.fallingBlockPos)));
            this.blockRenderManager.getModelRenderer().render(
                    fallingBlockEntityRenderState.world,
                    list,
                    blockState,
                    fallingBlockEntityRenderState.currentPos,
                    matrixStack,
                    new ColorableVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), 0x0000FF),
                    false,
                    OverlayTexture.DEFAULT_UV
                    );
            matrixStack.pop();
            //super.render(fallingBlockEntityRenderState, matrixStack, vertexConsumerProvider, i);
        }
    }*/

    public void render(ColoredFallingBlockEntityRenderState fallingBlockEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BlockState blockState = fallingBlockEntityRenderState.blockState;
        if (blockState.getRenderType() == BlockRenderType.MODEL) {
            matrixStack.push();
            matrixStack.translate((double)-0.5F, (double)0.0F, (double)-0.5F);
            int light = LightmapTextureManager.pack(15, 15);
            int color = fallingBlockEntityRenderState.color;

            List<BlockModelPart> list = this.blockRenderManager.getModel(blockState).getParts(Random.create(blockState.getRenderingSeed(fallingBlockEntityRenderState.fallingBlockPos)));

            BlockRenderType blockRenderType = blockState.getRenderType();
            if (blockRenderType != BlockRenderType.INVISIBLE) {
                BlockStateModel blockStateModel = blockRenderManager.getModel(blockState);
                float f = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float h = (float)(color & 255) / 255.0F;
                BlockModelRenderer.render(matrixStack.peek(), vertexConsumerProvider.getBuffer(RenderLayers.getEntityBlockLayer(blockState)), blockStateModel, f, g, h, light, OverlayTexture.DEFAULT_UV);
            }

            matrixStack.pop();
            super.render(fallingBlockEntityRenderState, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Override
    public ColoredFallingBlockEntityRenderState createRenderState() {
        return new ColoredFallingBlockEntityRenderState();
    }

    public void updateRenderState(ColoredFallingBlockEntity fallingBlockEntity, ColoredFallingBlockEntityRenderState fallingBlockEntityRenderState, float f) {
        super.updateRenderState(fallingBlockEntity, fallingBlockEntityRenderState, f);
        BlockPos blockPos = BlockPos.ofFloored(fallingBlockEntity.getX(), fallingBlockEntity.getBoundingBox().maxY, fallingBlockEntity.getZ());
        fallingBlockEntityRenderState.fallingBlockPos = fallingBlockEntity.getFallingBlockPos();
        fallingBlockEntityRenderState.currentPos = blockPos;
        fallingBlockEntityRenderState.blockState = fallingBlockEntity.getBlockState();
        fallingBlockEntityRenderState.world = fallingBlockEntity.getWorld();
        fallingBlockEntityRenderState.color = fallingBlockEntity.getColor();
    }
}