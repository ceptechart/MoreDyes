//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ianeli.moredyes.blocks;

import ianeli.moredyes.entity.ColoredFallingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ColoredFalling {
    default void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, ColoredFallingBlockEntity fallingBlockEntity) {
    }

    default void onDestroyedOnLanding(World world, BlockPos pos, ColoredFallingBlockEntity fallingBlockEntity) {
    }

    default DamageSource getDamageSource(Entity attacker) {
        return attacker.getDamageSources().fallingBlock(attacker);
    }
}
