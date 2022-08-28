package net.bobr.brewingmod.util;

import net.minecraft.nbt.NbtCompound;

public class AlcoholData {

    public static float addAlcohol(IEntityDataSaver player, float amount) {
        NbtCompound nbtCompound = player.getPersistentData();
        float alcohol = nbtCompound.getFloat("alcohol");
        if (alcohol + amount >= 10) {
            alcohol = 10;
        } else {
            alcohol += amount;
        }
        nbtCompound.putFloat("alcohol", alcohol);
        return alcohol;
    }

    public static float removeAlcohol(IEntityDataSaver player, float amount) {
        NbtCompound nbtCompound = player.getPersistentData();
        float alcohol = nbtCompound.getFloat("alcohol");
        if (alcohol - amount < 0) {
            alcohol = 0;
        } else {
            alcohol -= amount;
        }
        nbtCompound.putFloat("alcohol", alcohol);
        return alcohol;
    }
}
