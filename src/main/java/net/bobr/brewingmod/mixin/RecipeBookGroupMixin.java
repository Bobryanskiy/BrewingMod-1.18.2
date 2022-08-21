package net.bobr.brewingmod.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecipeBookGroup.class)
public class RecipeBookGroupMixin {
    @Inject(at = @At("HEAD"), method = "getGroups", cancellable = true)
    private static void recipeCategoryHook (RecipeBookCategory recipeBookCategory, CallbackInfoReturnable<List<RecipeBookGroup>> info) {
        if (recipeBookCategory == ClassTinkerers.getEnum(RecipeBookCategory.class, "OAK_BARREL")) {
            info.setReturnValue(RecipeBookGroup.BLAST_FURNACE);
        }
    }
}
