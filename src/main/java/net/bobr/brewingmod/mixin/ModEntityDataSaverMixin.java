package net.bobr.brewingmod.mixin;

import net.bobr.brewingmod.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("brewingmod.player_data", 10)) {
            persistentData = nbt.getCompound("brewingmod.player_data");
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod (NbtCompound nbt, CallbackInfoReturnable info) {
        if (persistentData != null) {
            nbt.put("brewingmod.player_data", persistentData);
        }
    }
}
