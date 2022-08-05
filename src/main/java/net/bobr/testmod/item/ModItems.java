package net.bobr.testmod.item;

import net.bobr.testmod.TestMod;
import net.bobr.testmod.block.ModBlocks;
import net.bobr.testmod.item.custom.Beer;
import net.bobr.testmod.item.custom.Mead;
import net.bobr.testmod.item.custom.Mug;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item MEAD = registerItem("mead",
            new Mead(ModBlocks.MEAD_BLOCK, new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.MEAD).maxDamage(3)));
    public static final Item MUG = registerItem("mug",
            new Mug(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item BEER = registerItem("beer",
            new Beer(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.BEER).maxCount(1)));
    public static final Item HOP_SEEDS = registerItem("hop_seeds",
            new AliasedBlockItem(ModBlocks.HOP_VINE, new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item HOP = registerItem("hop",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MALT = registerItem("malt",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item CARAMEL_MALT = registerItem("caramel_malt",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item YEAST = registerItem("yeast",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(TestMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        TestMod.LOGGER.info("Registering Mod Items for  " + TestMod.MOD_ID);
    }
}
