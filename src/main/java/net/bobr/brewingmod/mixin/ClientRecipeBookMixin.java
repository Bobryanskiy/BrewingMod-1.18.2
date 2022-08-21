package net.bobr.brewingmod.mixin;

import net.bobr.brewingmod.item.ModItemGroup;
import net.bobr.brewingmod.recipe.OakBarrelRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(at = @At("HEAD"), method = "getGroupForRecipe", cancellable = true)
    private static void recipeTypeHook (Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> info) {
        if (recipe.getType() == OakBarrelRecipe.Type.INSTANCE) {
            ItemGroup itemGroup = recipe.getOutput().getItem().getGroup();
            if (itemGroup == ModItemGroup.ALCOHOL) {
                info.setReturnValue(RecipeBookGroup.BLAST_FURNACE_BLOCKS);
            }
        }
    }
}
