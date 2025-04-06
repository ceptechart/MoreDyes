package ianeli.moredyes.blocks;

import com.mojang.serialization.MapCodec;
import ianeli.moredyes.ColorHandler;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.items.ItemConversion;
import ianeli.moredyes.items.ModItems;
import ianeli.moredyes.recipes.DyingRecipeBasic;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.awt.*;

public class DyeBasin extends GenericColoredBlock {
    public static final EnumProperty<Direction> FACING;
    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 12, 15);
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 3;
    public static final IntProperty LEVEL;
    public static final MapCodec<DyeBasin> CODEC = createCodec(DyeBasin::new);

    public DyeBasin(Settings settings) {
        super(settings);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Register the FACING property
        builder.add(LEVEL);
    }

    // Required override for HorizontalFacingBlock
    @Override
    public MapCodec<DyeBasin> getCodec() {
        return CODEC; // Use the parent's default CODEC
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Block belowBlock = world.getBlockState(pos.down()).getBlock();
        boolean heated = belowBlock == Blocks.FIRE || belowBlock == Blocks.MAGMA_BLOCK || belowBlock == Blocks.LAVA ||  belowBlock == Blocks.SOUL_FIRE;
        int level = state.get(DyeBasin.LEVEL);
        boolean playSound = (world.getTime() + pos.asLong()) % (200 + random.nextInt(800)) == 0;
        if (heated && level > 0 && playSound) {
            if (random.nextInt(100) > 50) {
                world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0f, 0.8f);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 1.0f, 0.8f);
            }

        }
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        if (!(world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity)) {
            return super.getPickStack(world, pos, state, includeData);
        }
        if (includeData) {
            ItemStack newDyeVial = new ItemStack(ModItems.DyeVialFilled);
            newDyeVial.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(coloredBlockEntity.getColor()));
            return newDyeVial;
        } else {
            return new ItemStack(this.asItem());
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World w, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (w instanceof ServerWorld world) {
            if (!(world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            ItemStack heldItem = player.getStackInHand(player.getActiveHand());
            Block belowBlock = world.getBlockState(pos.down()).getBlock();
            boolean heated = belowBlock == Blocks.FIRE || belowBlock == Blocks.MAGMA_BLOCK || belowBlock == Blocks.LAVA || belowBlock == Blocks.SOUL_FIRE;
            int level = state.get(DyeBasin.LEVEL);
            //Fill Basin
            if (heldItem.getItem() == Items.WATER_BUCKET) {
                if (level == 0) {
                    world.setBlockState(pos, state.with(LEVEL, 3));
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        player.setStackInHand(player.getActiveHand(), new ItemStack(Items.BUCKET));
                    }
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.FAIL;
                }
            //Rinse Item
            } else if (DyingRecipeBasic.canBasicDye(heldItem) && level > 0 && coloredBlockEntity.getColor() == 0) {
                ItemStack newItem = ItemConversion.toVanilla(heldItem);
                newItem.setCount(heldItem.getCount());

                player.setStackInHand(player.getActiveHand(), newItem);

                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1f, 1f);
                return ActionResult.SUCCESS;
            //Dye water with minecraft dye
            } else if (heldItem.getItem() instanceof DyeItem && heated && level > 0) {
                int newColor = ((DyeItem) heldItem.getItem()).getColor().getEntityColor();
                int oColor = coloredBlockEntity.getColor();
                if (oColor == 0) {
                    coloredBlockEntity.setColor(newColor);
                } else {
                    coloredBlockEntity.setColor(ColorHandler.mixColors(oColor, newColor));
                }
                heldItem.decrementUnlessCreative(1, player);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.PLAYERS, 1.0f, 0.8f);
                world.updateListeners(pos, state, state, 0);
                return ActionResult.SUCCESS;
            //Dye water with custom dye
            } else if (heldItem.getItem() == ModItems.DyeVialFilled && heated && level > 0) {
                int newColor = (heldItem.get(DataComponentTypes.DYED_COLOR).rgb());
                int oColor = coloredBlockEntity.getColor();
                if (oColor == 0) {
                    coloredBlockEntity.setColor(newColor);
                } else {
                    coloredBlockEntity.setColor(ColorHandler.mixColors(oColor, newColor));
                }
                heldItem.decrementUnlessCreative(1, player);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.PLAYERS, 1.0f, 0.8f);
                world.updateListeners(pos, state, state, 0);
                return ActionResult.SUCCESS;
            //Fill dye vial
            } else if (heldItem.getItem() == ModItems.DyeVial && level > 0 && coloredBlockEntity.getColor() != 0) {
                boolean resetColor = level == 1;
                heldItem.decrementUnlessCreative(1, player);
                ItemStack newDyeVial = new ItemStack(ModItems.DyeVialFilled);
                newDyeVial.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(coloredBlockEntity.getColor()));
                player.getInventory().insertStack(newDyeVial);
                if (player.getGameMode() != GameMode.CREATIVE) {
                    world.setBlockState(pos, state.with(LEVEL, level - 1));
                }
                if (resetColor) {
                    coloredBlockEntity.setColor(0);
                    world.updateListeners(pos, state, state, 0);
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
        LEVEL = IntProperty.of("level", MIN_LEVEL, MAX_LEVEL);
    }
}
