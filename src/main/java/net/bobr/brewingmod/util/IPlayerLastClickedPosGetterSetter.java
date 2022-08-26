package net.bobr.brewingmod.util;

import net.minecraft.util.math.BlockPos;

public interface IPlayerLastClickedPosGetterSetter {
    BlockPos getLastClickedPos();

    void setLastClickedPos(BlockPos pos);
}
