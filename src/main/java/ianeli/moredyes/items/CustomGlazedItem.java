package ianeli.moredyes.items;

import ianeli.moredyes.blocks.CustomGlazed;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;

public class CustomGlazedItem extends GenericColoredBlockItem{
    public CustomGlazedItem(Block block, Settings settings) {
        super(block, settings.component(DataComponentTypes.CUSTOM_MODEL_DATA, CustomGlazed.getStackRenderData(DyeColor.WHITE.getEntityColor())));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        DyedColorComponent dyedColorComponent = stack.get(DataComponentTypes.DYED_COLOR);
        if (dyedColorComponent != null) {
            stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, CustomGlazed.getStackRenderData(dyedColorComponent.rgb()));
        }
    }
}
