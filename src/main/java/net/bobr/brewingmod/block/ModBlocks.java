package net.bobr.brewingmod.block;

import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.block.custom.HopVineBlock;
import net.bobr.brewingmod.block.custom.MeadBlock;
import net.bobr.brewingmod.block.custom.OakBarrelBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block OAK_BARREL = registerBlock("oak_barrel",
            new OakBarrelBlock(FabricBlockSettings.of(Material.WOOD)
                    .strength(2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque()), ItemGroup.DECORATIONS);
    public static final Block HOP_VINE = registerBlockWithoutBlockItem("hop_vine",
            new HopVineBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));
    public static final Block MEAD_BLOCK = registerBlockWithoutBlockItem("mead_block",
            new MeadBlock(FabricBlockSettings.of(Material.DECORATION).nonOpaque().breakInstantly()));

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(BrewingMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(BrewingMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)) {
                    @Override
                    public void onItemEntityDestroyed(ItemEntity entity) {
                        ItemStack itemStack;
                        NbtCompound nbtCompound;
                        if (this.getBlock() instanceof OakBarrelBlock && (nbtCompound = BlockItem.getBlockEntityNbt(itemStack = entity.getStack())) != null && nbtCompound.contains("Items", 9)) {
                            NbtList nbtList = nbtCompound.getList("Items", 10);
                            ItemUsage.spawnItemContents(entity, nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt));
                        }
                        super.onItemEntityDestroyed(entity);
                    }
                });
    }

    private static Block registerBlockWithoutBlockItem (String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(BrewingMod.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        BrewingMod.LOGGER.info("Registering ModBlocks for " + BrewingMod.MOD_ID);
    }
}
