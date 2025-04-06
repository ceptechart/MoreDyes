package ianeli.moredyes.mixin.client;

import ianeli.moredyes.CustomSheep;
import ianeli.moredyes.Entity.CustomSheepRenderState;
import ianeli.moredyes.Entity.CustomSheepRenderer;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntityRenderer.class)
public class SheepEntityRendererMixin implements CustomSheepRenderer {
    @Inject(method = "updateRenderState*", at = @At("TAIL"))
    private void onUpdateRenderState(SheepEntity sheep, SheepEntityRenderState renderState, float f, CallbackInfo ci) {
        if (sheep instanceof CustomSheep customSheep) {
            if (renderState instanceof CustomSheepRenderState customSheepRenderState) {
                customSheepRenderState.setCustomColor(customSheep.getCustomColor());
            }
        }
    }
}