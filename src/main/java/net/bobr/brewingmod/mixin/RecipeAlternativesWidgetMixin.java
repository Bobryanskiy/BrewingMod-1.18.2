package net.bobr.brewingmod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.screen.OakBarrelScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Iterator;

@Mixin(RecipeAlternativesWidget.class)
public class RecipeAlternativesWidgetMixin {
    @Shadow
    MinecraftClient client;
    private static final Identifier MOD_BACKGROUND_TEXTURE = new Identifier(BrewingMod.MOD_ID, "textures/gui/mod_recipe_book.png");
    @Shadow
    float time;

    @ModifyArg(
        method = "showAlternativesForResult(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/recipebook/RecipeResultCollection;IIIIF)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
        )
    )
    public Object myBlockHook (Object widget) {
        if (client.player.currentScreenHandler instanceof OakBarrelScreenHandler) {
            RecipeAlternativesWidget.AlternativeButtonWidget widget1 = (RecipeAlternativesWidget.AlternativeButtonWidget) widget;
            return new RecipeAlternativesWidget(). new AlternativeButtonWidget(widget1.x, widget1.y, ((IAlternativeButtonWidgetGetter) widget1).getRecipe(), ((IAlternativeButtonWidgetGetter) widget1).getCraftable()) {
                @Override
                protected void alignRecipe (Recipe <?> recipe){
                    this.alignRecipeToGrid(2, 3, -1, recipe, recipe.getIngredients().iterator(), 0);
                }

                @Override
                public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                    int j;
                    RenderSystem.setShaderTexture(0, MOD_BACKGROUND_TEXTURE);
                    int i = 0;
                    if (!((IAlternativeButtonWidgetGetter) widget1).getCraftable()) {
                        i += 26;
                    }
                    int n = j = 37;
                    if (this.isHovered()) {
                        j += 26;
                    }
                    this.drawTexture(matrices, this.x, this.y, i, j, this.width, this.height);
                    MatrixStack matrixStack = RenderSystem.getModelViewStack();
                    matrixStack.push();
                    matrixStack.translate(this.x + 2, this.y + 2, 125.0);
                    for (InputSlot inputSlot : this.slots) {
                        matrixStack.push();
                        matrixStack.translate(inputSlot.y, inputSlot.x, 0.0);
                        matrixStack.scale(0.375f, 0.375f, 1.0f);
                        matrixStack.translate(-8.0, -8.0, 0.0);
                        RenderSystem.applyModelViewMatrix();
                        client.getItemRenderer().renderInGuiWithOverrides(inputSlot.stacks[MathHelper.floor(time / 30.0f) % inputSlot.stacks.length], 0, 0);
                        matrixStack.pop();
                    }
                    matrixStack.pop();
                    RenderSystem.applyModelViewMatrix();
                }

                @Override
                public void acceptAlignedInput(Iterator<Ingredient> inputs, int slot, int amount, int gridX, int gridY) {
                    ItemStack[] itemStacks = inputs.next().getMatchingStacks();
                    if (itemStacks.length != 0) {
                        this.slots.add(new InputSlot(6 + gridY * 9, 3 + gridX * 7, itemStacks));
                    }
                }
            };
        } else {
            return widget;
        }
    }
}
