package ianeli.moredyes.items;

import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GenericColoredBlockItem extends BlockItem {

    public GenericColoredBlockItem(Block block, Settings settings) {
        super(block, settings.component(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0x3F76E4)));
    }
}
