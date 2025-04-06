package ianeli.moredyes;

import ianeli.moredyes.blockEntity.ModBlockEntities;
import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.entity.ModEntities;
import ianeli.moredyes.items.ItemConversion;
import ianeli.moredyes.items.ModItems;
import ianeli.moredyes.misc.ModEvents;
import ianeli.moredyes.misc.ModLootTables;
import ianeli.moredyes.network.ModNetworking;
import ianeli.moredyes.recipes.ModRecipes;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDyes implements ModInitializer {
	public static final String MOD_ID = "moredyes";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModRecipes.initialize();
		ModEvents.initialize();
		ModItemGroup.initialize();
		ModEntities.initialize();
		ModNetworking.initialize();
		ModLootTables.initialize();
		ItemConversion.initialize();
	}
}