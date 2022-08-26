package net.bobr.brewingmod.mixin;

import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeAlternativesWidget.AlternativeButtonWidget.class)
public interface IAlternativeButtonWidgetGetter {
    @Accessor
    boolean getCraftable();
    @Accessor
    Recipe<?> getRecipe();
}
