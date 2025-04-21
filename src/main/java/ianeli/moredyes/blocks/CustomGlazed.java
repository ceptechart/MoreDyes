package ianeli.moredyes.blocks;

import ianeli.moredyes.ColorHandler;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.items.DyeVial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomGlazed extends GenericColoredBlock{
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty TYPE = IntProperty.of("type", 0, 16);

    public CustomGlazed(Settings settings) {
        super(settings);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise()).with(TYPE, getType(ctx.getStack().get(DataComponentTypes.DYED_COLOR).rgb()));
    }

    public static int getType(int c) {
        for (DyedColorComponent color : DyeVial.PureColors.keySet()) {
            if (c == color.rgb()) {
                return 6;
            }
        }
        return Math.floorMod(ColorHandler.hash32shift(c), 16);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Register the FACING property
        builder.add(TYPE);
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }


    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);

        BlockEntity be = builder.get(LootContextParameters.BLOCK_ENTITY);
        if (!(be instanceof ColoredBlockEntity coloredBlockEntity)) {
            return drops;
        }

        for (ItemStack stack : drops) {
            stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, getStackRenderData(coloredBlockEntity.getColor()));
        }

        return drops;
    }
    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        if (!(world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity)) {
            return super.getPickStack(world, pos, state, includeData);
        }
        ItemStack pickitem = this.asItem().getDefaultStack();
        pickitem.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(coloredBlockEntity.getColor()));
        pickitem.set(DataComponentTypes.CUSTOM_MODEL_DATA, getStackRenderData(coloredBlockEntity.getColor()));
        return pickitem;
    }

    public static CustomModelDataComponent getStackRenderData(int color) {
        List<Integer> colorData = new ArrayList<>();
        List<String> stringData = new ArrayList<>();

        stringData.add(String.valueOf(getType(color)));
        colorData.add(color);
        colorData.add(ColorHandler.getGlazedAccent(color));

        return new CustomModelDataComponent(
                new ArrayList<Float>(),
                new ArrayList<Boolean>(),
                stringData,
                colorData
        );
    }
}
