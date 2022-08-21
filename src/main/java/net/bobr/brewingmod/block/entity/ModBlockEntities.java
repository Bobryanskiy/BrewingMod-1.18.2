package net.bobr.brewingmod.block.entity;

import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<OakBarrelBlockEntity> OAK_BARREL;
    public static BlockEntityType<MeadBlockEntity> MEAD_BLOCK;

    public static void registerAllBlockEntities() {
        OAK_BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BrewingMod.MOD_ID, "oak_barrel"),
                FabricBlockEntityTypeBuilder.create(OakBarrelBlockEntity::new,
                        ModBlocks.OAK_BARREL).build(null));
        MEAD_BLOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BrewingMod.MOD_ID, "mead_block"),
                FabricBlockEntityTypeBuilder.create(MeadBlockEntity::new,
                        ModBlocks.MEAD_BLOCK).build(null));
    }
}
