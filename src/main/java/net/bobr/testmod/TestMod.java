package net.bobr.testmod;

import net.bobr.testmod.block.ModBlocks;
import net.bobr.testmod.block.entity.ModBlockEntities;
import net.bobr.testmod.item.ModItems;
import net.bobr.testmod.loot.ModLootTableModifiers;
import net.bobr.testmod.recipe.ModRecipes;
import net.bobr.testmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMod implements ModInitializer {
	public static final String MOD_ID = "testmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerAllBlockEntities();
		ModScreenHandlers.registerAllScreenHandlers();
		ModRecipes.registerRecipes();
		ModLootTableModifiers.registerLootTableModifier();
	}
}
