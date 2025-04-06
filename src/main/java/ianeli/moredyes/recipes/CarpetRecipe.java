package ianeli.moredyes.recipes;

import ianeli.moredyes.blocks.ModBlocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CarpetRecipe extends SpecialCraftingRecipe {

    public CarpetRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() { return ModRecipes.CARPET_RECIPE; }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int gridSizeX = input.getWidth();
        int gridSizeY = input.getHeight();
        int pairX = -1, pairY = -1;

        // First find the pair
        for (int y = 0; y < gridSizeY; y++) {
            for (int x = 0; x < gridSizeX - 1; x++) {
                ItemStack a = input.getStackInSlot(x, y);
                ItemStack b = input.getStackInSlot(x + 1, y);
                if (a != null && b != null && areSameWool(a, b)) {
                    // If we already found a pair, return false
                    if (pairX != -1) return false;
                    pairX = x;
                    pairY = y;
                    x++; // Skip next item
                }
            }
        }

        // If no pair found, return false
        if (pairX == -1) return false;

        // Verify all other slots are empty
        for (int y = 0; y < gridSizeY; y++) {
            for (int x = 0; x < gridSizeX; x++) {
                // Skip the pair we found
                if ((y == pairY && (x == pairX || x == pairX + 1))) continue;

                if (input.getStackInSlot(x, y) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean areSameWool(ItemStack curr, ItemStack next) {
        return curr.isOf(ModBlocks.CustomWool.asItem()) && next.isOf(curr.getItem()) && curr.get(DataComponentTypes.DYED_COLOR).rgb() == next.get(DataComponentTypes.DYED_COLOR).rgb();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack crafted = new ItemStack(ModBlocks.CustomCarpet.asItem());
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.getItem() == ModBlocks.CustomWool.asItem()) {
                int c = stack.get(DataComponentTypes.DYED_COLOR).rgb();
                crafted.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(c));
                break;
            }
        }
        crafted.setCount(3);
        return crafted;
    }


}
