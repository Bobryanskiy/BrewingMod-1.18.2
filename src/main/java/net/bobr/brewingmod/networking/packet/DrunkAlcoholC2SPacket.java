package net.bobr.brewingmod.networking.packet;

import net.bobr.brewingmod.util.AlcoholData;
import net.bobr.brewingmod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DrunkAlcoholC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        AlcoholData.addAlcohol((IEntityDataSaver) player, buf.readFloat());
        player.sendMessage((Text.of("" + ((IEntityDataSaver) player).getPersistentData().getFloat("alcohol"))), false);
    }
}

