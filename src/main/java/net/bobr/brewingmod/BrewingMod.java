package net.bobr.brewingmod;

import net.bobr.brewingmod.block.ModBlocks;
import net.bobr.brewingmod.block.entity.ModBlockEntities;
import net.bobr.brewingmod.event.PlayerTickHandler;
import net.bobr.brewingmod.item.ModItems;
import net.bobr.brewingmod.loot.ModLootTableModifiers;
import net.bobr.brewingmod.networking.ModPackets;
import net.bobr.brewingmod.recipe.ModRecipes;
import net.bobr.brewingmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrewingMod implements ModInitializer {
	public static final String MOD_ID = "brewingmod";
	public static final Logger LOGGER = LoggerFactory.getLogger("Brewing Mod");

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerAllBlockEntities();
		ModScreenHandlers.registerAllScreenHandlers();
		ModRecipes.registerRecipes();
		ModLootTableModifiers.registerLootTableModifier();
		ModPackets.registerC2SPackets();
		ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
	}
}
