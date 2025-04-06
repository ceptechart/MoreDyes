package ianeli.moredyes.recipes;

import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.items.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class DyingRecipeBasic extends SpecialCraftingRecipe {
    public DyingRecipeBasic(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.BASIC_DYE;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack toDye = null;
        ItemStack dye = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() == ModItems.DyeVialFilled) {
                if (dye != null) return false;
                dye = stack;
            } else if (canBasicDye(stack)) {
                if (toDye != null) return false;
                toDye = stack;
            } else {
                return false;
            }
        }

        return toDye != null && dye != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack toDye = null;
        Integer color = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() == ModItems.DyeVialFilled) {
                color = stack.get(DataComponentTypes.DYED_COLOR).rgb();
            } else if (canBasicDye(stack)) {
                toDye = stack.copy();
            }
        }

        if (toDye == null || color == null) return ItemStack.EMPTY;

        return applyColor(toDye, color);
    }

    public static boolean canBasicDye(ItemStack stack) {
        Item item  = stack.getItem();
        return item == ModBlocks.CustomTerracotta.asItem()
        || item == ModBlocks.CustomWool.asItem() || item == ModBlocks.CustomCarpet.asItem()
        || item == ModBlocks.CustomConcrete.asItem() || item == ModBlocks.CustomConcretePowder.asItem()
        || item == ModBlocks.CustomGlass.asItem() || item == ModBlocks.CustomGlassPane.asItem()
        || item == Items.LEATHER_BOOTS || item == Items.LEATHER_LEGGINGS || item == Items.LEATHER_CHESTPLATE || item == Items.LEATHER_HELMET || item == Items.LEATHER_HORSE_ARMOR || item == Items.WOLF_ARMOR;
    }
    private ItemStack applyColor(ItemStack stack, int color) {
        stack.setCount(1);
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color));
        return stack;
    }
}
