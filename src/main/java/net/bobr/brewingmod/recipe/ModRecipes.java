package net.bobr.brewingmod.recipe;

import net.bobr.brewingmod.BrewingMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BrewingMod.MOD_ID, OakBarrelRecipe.Serializer.ID),
                OakBarrelRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(BrewingMod.MOD_ID, OakBarrelRecipe.Type.ID),
                OakBarrelRecipe.Type.INSTANCE);
    }
}
