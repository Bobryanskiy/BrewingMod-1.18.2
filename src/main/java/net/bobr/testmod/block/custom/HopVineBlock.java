package net.bobr.testmod.block.custom;

import net.bobr.testmod.item.ModItems;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;

public class HopVineBlock extends CropBlock {
    public HopVineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.HOP_SEEDS;
    }
}
