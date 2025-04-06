package ianeli.moredyes.misc;

import ianeli.moredyes.CustomSheep;
import ianeli.moredyes.blocks.ModBlocks;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

public class ModEvents {
    public static void initialize() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((serverWorld, entity, livingEntity) -> {
            if (livingEntity instanceof SheepEntity sheep && livingEntity instanceof CustomSheep customSheep) {
                int c = customSheep.getCustomColor();
                ItemStack stack = ModBlocks.CustomWool.asItem().getDefaultStack();
                stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(c));
                if (!sheep.isSheared() && !sheep.isBaby()) {
                    sheep.dropStack(serverWorld, stack.copyWithCount(1), 1.0f);
                }
            }
        });
    }
}
