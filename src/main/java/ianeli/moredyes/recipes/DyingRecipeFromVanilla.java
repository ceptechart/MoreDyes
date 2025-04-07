package ianeli.moredyes.recipes;

import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.items.ItemConversion;
import ianeli.moredyes.items.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class DyingRecipeFromVanilla extends SpecialCraftingRecipe {
    public DyingRecipeFromVanilla(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.VANILLA_DYE;
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

        RegistryEntry<Item> entry = Registries.ITEM.getEntry(stack.getItem());

        return stack.isIn(ItemTags.WOOL) || stack.isIn(ItemTags.WOOL_CARPETS) || stack.isIn(ItemTags.TERRACOTTA)
                || stack.isIn(ItemConversion.CONCRETE_POWDER) || stack.isIn(ItemConversion.CONCRETE)
                || stack.isIn(ItemConversion.STAINED_GLASS) || stack.isIn(ItemConversion.STAINED_GLASS_PANE)
                || stack.getItem() == Items.GLASS || stack.getItem() == Items.GLASS_PANE;
    }
    private ItemStack applyColor(ItemStack stack, int color) {
        ItemStack newStack = ItemConversion.toModded(stack);
        newStack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color));
        return newStack;
    }
}
