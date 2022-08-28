package net.bobr.brewingmod.event;

import net.bobr.brewingmod.util.AlcoholData;
import net.bobr.brewingmod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

public class PlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (new Random().nextFloat() <= 0.005f) {
                IEntityDataSaver dataSaver = (IEntityDataSaver) player;
                AlcoholData.removeAlcohol(dataSaver, 1.0f);
                player.sendMessage(Text.of("s"), false);
            }
        }
    }
}
