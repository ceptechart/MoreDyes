package ianeli.moredyes.mixin.client;

import ianeli.moredyes.Entity.CustomSheepRenderState;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SheepEntityRenderState.class)
public class SheepEntityRenderStateMixin implements CustomSheepRenderState {

    @Unique
    public int CustomColor;

    @Override
    public int getCustomColor(int i) {
        return CustomColor;
    }

    @Override
    public void setCustomColor(int i) {
        CustomColor = i;
    }

    /**
     * @author me
     * @reason custom sheep color :)
     */
    @Overwrite
    public int getRgbColor() {
        return CustomColor;
    }
}