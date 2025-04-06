package ianeli.moredyes.misc;

import ianeli.moredyes.items.ModItems;
import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public class ModLootTables {

    public static LootPool.Builder DyeItemsLootPool = LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .conditionally(RandomChanceLootCondition.builder(0.5f))
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0xFF0000)))) //Pure Red
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0xFFFF00)))) //Pure Yellow
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0x00FF00)))) //Pure Green
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0x00FFFF)))) //Pure Cyan
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0x0000FF)))) //Pure Blue
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0xFF00FF)))) //Pure Magenta
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0xFFFFFF)))) //Pure White
            .with(ItemEntry.builder(ModItems.DyeVialFilled).apply(SetComponentsLootFunction.builder(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0x101010)))); //Pure Black

    public static List<RegistryKey<LootTable>> lootTables = List.of(
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.ANCIENT_CITY_CHEST,
            LootTables.BASTION_TREASURE_CHEST,
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.SHIPWRECK_SUPPLY_CHEST,
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.STRONGHOLD_CORRIDOR_CHEST,
            LootTables.WOODLAND_MANSION_CHEST,
            LootTables.VILLAGE_CARTOGRAPHER_CHEST,
            LootTables.VILLAGE_TANNERY_CHEST,
            LootTables.VILLAGE_SHEPARD_CHEST,
            LootTables.TRIAL_CHAMBERS_REWARD_COMMON_CHEST,
            LootTables.TRIAL_CHAMBERS_REWARD_RARE_CHEST
    );

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin()) {
                for (RegistryKey<LootTable> table : lootTables) {
                    if (key == table) {
                        tableBuilder.pool(DyeItemsLootPool);
                    }
                }
            }
        });
    }
}
