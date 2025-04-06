package ianeli.moredyes.items;

import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemConversion {

    public static final TagKey<Item> STAINED_GLASS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, "stained_glass"));
    public static final TagKey<Item> STAINED_GLASS_PANE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, "stained_glass_pane"));
    public static final TagKey<Item> CONCRETE = TagKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, "concrete"));
    public static final TagKey<Item> CONCRETE_POWDER = TagKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, "concrete_powder"));

    public static ItemStack toModded(ItemStack from) {
        if (from.isIn(ItemTags.WOOL)) {
            return ModBlocks.CustomWool.asItem().getDefaultStack();
        } else if (from.isIn(ItemTags.WOOL_CARPETS)) {
            return ModBlocks.CustomCarpet.asItem().getDefaultStack();
        } else if (from.isIn(ItemTags.TERRACOTTA)) {
            return ModBlocks.CustomTerracotta.asItem().getDefaultStack();
        } else if (from.isIn(STAINED_GLASS) || from.getItem() == Items.GLASS) {
            return ModBlocks.CustomGlass.asItem().getDefaultStack();
        } else if (from.isIn(STAINED_GLASS_PANE) || from.getItem() == Items.GLASS_PANE) {
            return ModBlocks.CustomGlassPane.asItem().getDefaultStack();
        } else if (from.isIn(CONCRETE)) {
            return ModBlocks.CustomConcrete.asItem().getDefaultStack();
        } else if (from.isIn(CONCRETE_POWDER)) {
            return ModBlocks.CustomConcretePowder.asItem().getDefaultStack();
        }
        return from;
    }

    public static ItemStack toVanilla(ItemStack from) {
        Item item = from.getItem();
        if (item == ModBlocks.CustomWool.asItem()) {
            return Blocks.WHITE_WOOL.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomCarpet.asItem()) {
            return Blocks.WHITE_CARPET.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomTerracotta.asItem()) {
            return Blocks.TERRACOTTA.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomGlass.asItem()) {
            return Blocks.GLASS.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomGlassPane.asItem()) {
            return Blocks.GLASS_PANE.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomConcrete.asItem()) {
            return Blocks.WHITE_CONCRETE.asItem().getDefaultStack();
        } else if (item == ModBlocks.CustomConcretePowder.asItem()) {
            return Blocks.WHITE_CONCRETE_POWDER.asItem().getDefaultStack();
        }
        return from;
    }

    public static void initialize() {
    }
}
