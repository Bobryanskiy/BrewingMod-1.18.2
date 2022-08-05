package net.bobr.testmod.recipe;

import net.bobr.testmod.TestMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(TestMod.MOD_ID, OakBarrelRecipe.Serializer.ID),
                OakBarrelRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(TestMod.MOD_ID, OakBarrelRecipe.Type.ID),
                OakBarrelRecipe.Type.INSTANCE);
    }
}
