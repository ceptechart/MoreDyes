package ianeli.moredyes.blocks;

import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.items.GenericColoredBlockItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModBlocks {
    private static Block registerNewBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean registerItem, BiFunction<Block, Item.Settings, BlockItem> blockItemFactory) {
        RegistryKey<Block> blockRegistryKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockRegistryKey));
        if (registerItem) {
            RegistryKey<Item> itemRegistryKey = keyOfItem(name);
            BlockItem blockItem = blockItemFactory.apply(block, new Item.Settings().registryKey(itemRegistryKey));
            Registry.register(Registries.ITEM, itemRegistryKey, blockItem);
        }
        return Registry.register(Registries.BLOCK, blockRegistryKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MoreDyes.MOD_ID, name));
    }
    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, name));
    }

    public static final Block DyeBasin = registerNewBlock(
            "dyebasin",
            ianeli.moredyes.blocks.DyeBasin::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).nonOpaque().ticksRandomly().requiresTool().strength(5.0F, 1200.0F),
            true,
            BlockItem::new
    );

    public static final Block CustomWool = registerNewBlock(
            "wool",
            CustomWool::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_WOOL),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomCarpet = registerNewBlock(
            "carpet",
            CustomCarpet::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_CARPET).nonOpaque(),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomGlass = registerNewBlock(
            "glass",
            CustomGlass::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_STAINED_GLASS),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomGlassPane = registerNewBlock(
      "glasspane",
            CustomPane::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_STAINED_GLASS_PANE),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomTerracotta = registerNewBlock(
            "terracotta",
            GenericColoredBlock::new,
            AbstractBlock.Settings.copy(Blocks.TERRACOTTA),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomConcrete = registerNewBlock(
            "concrete",
            GenericColoredBlock::new,
            AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE),
            true,
            GenericColoredBlockItem::new
    );
    public static final Block CustomConcretePowder = registerNewBlock(
            "concrete_powder",
            CustomPowder::new,
            AbstractBlock.Settings.copy(Blocks.SAND),
            true,
            GenericColoredBlockItem::new
    );
    public static void initialize() {

    }
}
