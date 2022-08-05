package net.bobr.testmod.block.entity;

import net.bobr.testmod.TestMod;
import net.bobr.testmod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<OakBarrelBlockEntity> OAK_BARREL;

    public static void registerAllBlockEntities() {
        OAK_BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(TestMod.MOD_ID, "oak_barrel"),
                FabricBlockEntityTypeBuilder.create(OakBarrelBlockEntity::new,
                        ModBlocks.OAK_BARREL).build(null));
    }
}
