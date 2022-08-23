package net.bobr.brewingmod.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(RecipeBookGroup.class)
public class RecipeBookGroupMixin {
    @Inject(at = @At("HEAD"), method = "getGroups", cancellable = true)
    private static void recipeCategoryHook (RecipeBookCategory recipeBookCategory, CallbackInfoReturnable<List<RecipeBookGroup>> info) {
        if (recipeBookCategory == ClassTinkerers.getEnum(RecipeBookCategory.class, "OAK_BARREL")) {
            info.setReturnValue(ImmutableList.of(ClassTinkerers.getEnum(RecipeBookGroup.class, "ALCOHOL_DRINKS")));
        }
    }
//    @Shadow
//    @Final
//    @Mutable
//    private static Map<RecipeBookGroup, List<RecipeBookGroup>> SEARCH_MAP;
//
//    static {
//        SEARCH_MAP = ImmutableMap.<RecipeBookGroup, List<RecipeBookGroup>> builder()
//                .putAll(SEARCH_MAP)
//                .put(ClassTinkerers.getEnum(RecipeBookGroup.class, "OAK_BARREL"), ImmutableList.of())
//                .build();
//    }
}
