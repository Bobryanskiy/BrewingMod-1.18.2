package net.bobr.brewingmod.item;

import net.bobr.brewingmod.BrewingMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup ALCOHOL = FabricItemGroupBuilder.build(new Identifier(BrewingMod.MOD_ID, "alcohol"),
            () -> new ItemStack(ModItems.MEAD));
}
