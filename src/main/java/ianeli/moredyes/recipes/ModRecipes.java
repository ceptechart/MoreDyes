package ianeli.moredyes.recipes;

import ianeli.moredyes.MoreDyes;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final RecipeSerializer<DyingRecipeBasic> BASIC_DYE =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(MoreDyes.MOD_ID, "basic_dying"),
                    new SpecialCraftingRecipe.SpecialRecipeSerializer(DyingRecipeBasic::new)
            );

    public static final RecipeType<DyingRecipeBasic> BASIC_DYE_TYPE =
            Registry.register(Registries.RECIPE_TYPE,
                    Identifier.of(MoreDyes.MOD_ID, "basic_dying"),
                    new RecipeType<DyingRecipeBasic>() {
                        @Override
                        public String toString() {
                            return "moredyes:basic_dying";
                        }
                    });

    public static final RecipeSerializer<CarpetRecipe> CARPET_RECIPE =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(MoreDyes.MOD_ID, "carpet"),
                    new SpecialCraftingRecipe.SpecialRecipeSerializer(CarpetRecipe::new)
            );

    public static final RecipeType<CarpetRecipe> CARPET_RECIPE_TYPE =
            Registry.register(Registries.RECIPE_TYPE,
                    Identifier.of(MoreDyes.MOD_ID, "carpet"),
                    new RecipeType<CarpetRecipe>() {
                        @Override
                        public String toString() {
                            return "moredyes:carpet";
                        }
                    });

    public static final RecipeSerializer<DyingRecipeFromVanilla> VANILLA_DYE =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(MoreDyes.MOD_ID, "vanilla_dying"),
                    new SpecialCraftingRecipe.SpecialRecipeSerializer(DyingRecipeFromVanilla::new)
            );

    public static final RecipeType<DyingRecipeFromVanilla> VANILLA_DYE_TYPE =
            Registry.register(Registries.RECIPE_TYPE,
                    Identifier.of(MoreDyes.MOD_ID, "vanilla_dying"),
                    new RecipeType<DyingRecipeFromVanilla>() {
                        @Override
                        public String toString() {
                            return "moredyes:vanilla_dying";
                        }
                    });

    public static void initialize() {
    }
}
