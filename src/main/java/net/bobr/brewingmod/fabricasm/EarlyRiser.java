package net.bobr.brewingmod.fabricasm;

import com.chocohead.mm.api.ClassTinkerers;
import net.bobr.brewingmod.item.ModItems;
import net.minecraft.item.ItemStack;

public class EarlyRiser implements Runnable {

    @Override
    public void run() {
        ClassTinkerers.enumBuilder("net/minecraft/recipe/book/RecipeBookCategory").addEnum("OAK_BARREL").build();

        ClassTinkerers.enumBuilder("net/minecraft/client/recipebook/RecipeBookGroup", "[Lnet/minecraft/item/ItemStack;").addEnum("ALCOHOL_DRINKS",
                () -> new Object[]{new ItemStack[]{new ItemStack(ModItems.MEAD)}}).build();
    }
}
