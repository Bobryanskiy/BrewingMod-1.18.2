package net.bobr.brewingmod.mixin;

import net.bobr.brewingmod.util.IPlayerLastClickedPosGetterSetter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerLastClickedPosGetterSetter {
    private BlockPos lastClickedPos;

    @Override
    public BlockPos getLastClickedPos() {
        return lastClickedPos;
    }

    @Override
    public void setLastClickedPos(BlockPos pos) {
        lastClickedPos = pos;
    }
}
