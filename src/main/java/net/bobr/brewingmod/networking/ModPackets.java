package net.bobr.brewingmod.networking;

import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.networking.packet.DrunkAlcoholC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static Identifier DRUNK_ALCOHOL = new Identifier(BrewingMod.MOD_ID, "drunk_alcohol");
    public static Identifier THIRST_SYNC_ID = new Identifier(BrewingMod.MOD_ID, "thirst_id");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DRUNK_ALCOHOL, DrunkAlcoholC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}
