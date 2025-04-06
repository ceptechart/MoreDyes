package ianeli.moredyes.mixin.client;

import ianeli.moredyes.ColorHandler;
import ianeli.moredyes.blocks.ModBlocks;
import net.minecraft.client.render.item.tint.DyeTintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DyeTintSource.class)
public class DyeTintSourceMixin {
    @Inject(method="getTint", at = @At("HEAD"), cancellable = true)
    public void getTint(ItemStack stack, ClientWorld world, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() == ModBlocks.CustomTerracotta.asItem()) {
            int c = DyedColorComponent.getColor(stack, 0x000000);
            int c2 = ColorHandler.getTerraCotta(c);
            cir.setReturnValue(c2);
        }
    }
}
