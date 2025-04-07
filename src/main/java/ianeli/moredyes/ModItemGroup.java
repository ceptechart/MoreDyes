package ianeli.moredyes;

import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.items.DyeVial;
import ianeli.moredyes.items.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModItemGroup {

    public static int[] dyeColors = {
            DyeColor.WHITE.getEntityColor(),
            DyeColor.LIGHT_GRAY.getEntityColor(),
            DyeColor.GRAY.getEntityColor(),
            DyeColor.BLACK.getEntityColor(),
            DyeColor.BROWN.getEntityColor(),
            DyeColor.RED.getEntityColor(),
            DyeColor.ORANGE.getEntityColor(),
            DyeColor.YELLOW.getEntityColor(),
            DyeColor.LIME.getEntityColor(),
            DyeColor.GREEN.getEntityColor(),
            DyeColor.CYAN.getEntityColor(),
            DyeColor.LIGHT_BLUE.getEntityColor(),
            DyeColor.BLUE.getEntityColor(),
            DyeColor.PURPLE.getEntityColor(),
            DyeColor.MAGENTA.getEntityColor(),
            DyeColor.PINK.getEntityColor(),
    };

    public static void addStandardDyeVariants(ItemGroup.Entries entries, ItemStack itemStack) {
        for (int c : dyeColors) {
            ItemStack stack = itemStack.copy();
            stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(c));
            entries.add(stack);
        }
    }
    public static void addWhite(ItemGroup.Entries entries, ItemStack itemStack) {
        itemStack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyeColor.WHITE.getEntityColor()));
        entries.add(itemStack);
    }
    public static void addPureColors(ItemGroup.Entries entries) {
        for (DyedColorComponent color : DyeVial.PureColors.keySet()) {
            ItemStack stack = ModItems.DyeVialFilled.getDefaultStack();
            stack.set(DataComponentTypes.DYED_COLOR, color);
            entries.add(stack);
        }
    }

    public static final ItemGroup MoreDyesGroup = FabricItemGroup.builder()
            .icon(() -> {
                ItemStack stack = new ItemStack(ModBlocks.CustomWool.asItem());
                stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(ColorHandler.getRandomSaturatedColor()));
                return stack;
            }).displayName(Text.translatable("itemGroup.moredyes.main_group"))
            .entries((context, entries) -> {
                addPureColors(entries);
                addWhite(entries, ModBlocks.CustomWool.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomCarpet.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomTerracotta.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomConcrete.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomConcretePowder.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomGlass.asItem().getDefaultStack());
                addWhite(entries, ModBlocks.CustomGlassPane.asItem().getDefaultStack());
                entries.add(ModBlocks.DyeBasin.asItem().getDefaultStack());
                entries.add(ModItems.DyeVial.getDefaultStack());
            }).build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of(MoreDyes.MOD_ID, "main_group"), MoreDyesGroup);
    }
}
