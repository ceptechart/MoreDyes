package ianeli.moredyes;

import net.minecraft.block.StonecutterBlock;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlazedItemRenderer extends ItemRenderer {
    public GlazedItemRenderer(ItemModelManager itemModelManager) {
        super(itemModelManager);
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext displayContext, int light, int overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable World world, int seed) {
        this.renderItem((LivingEntity)null, Items.SUNFLOWER.getDefaultStack(), displayContext, matrices, vertexConsumers, world, light, overlay, seed);
    }
}
