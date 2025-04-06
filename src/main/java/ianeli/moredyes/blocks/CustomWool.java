package ianeli.moredyes.blocks;

import ianeli.moredyes.MoreDyes;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class CustomWool extends GenericColoredBlock{

    public CustomWool(Settings settings) {
        super(settings);
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        ItemStack heldItem = player.getMainHandStack();
        if (heldItem.getItem() instanceof ShearsItem) {
            return 0.15f; // Instantly breaks the block (adjust as needed)
        }
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }
}
