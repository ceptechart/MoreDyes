package ianeli.moredyes.items;

import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GenericColoredBlockItem extends BlockItem {

    public GenericColoredBlockItem(Block block, Settings settings) {
        super(block, settings);
    }
    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        if (world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity) {
            coloredBlockEntity.setColor(stack.get(DataComponentTypes.DYED_COLOR).rgb());
        }
        return super.postPlacement(pos, world, player, stack, state);
    }
}
