package net.bobr.brewingmod.screen;

import net.bobr.brewingmod.BrewingMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<OakBarrelScreenHandler> OAK_BARREL_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        OAK_BARREL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(BrewingMod.MOD_ID, "oak_barrel"),
                OakBarrelScreenHandler::new);
    }
}
