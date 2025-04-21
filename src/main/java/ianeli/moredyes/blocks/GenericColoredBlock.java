package ianeli.moredyes.blocks;

import com.mojang.serialization.MapCodec;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GenericColoredBlock extends BlockWithEntity {
    public GenericColoredBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(GenericColoredBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ColoredBlockEntity(pos, state);
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        if (!(world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity)) {
            return super.getPickStack(world, pos, state, includeData);
        }
        ItemStack pickitem = this.asItem().getDefaultStack();
        pickitem.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(coloredBlockEntity.getColor()));
        return pickitem;
    }
}
