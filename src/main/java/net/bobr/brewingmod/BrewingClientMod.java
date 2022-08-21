package net.bobr.brewingmod;

import net.bobr.brewingmod.block.ModBlocks;
import net.bobr.brewingmod.screen.ModScreenHandlers;
import net.bobr.brewingmod.screen.OakBarrelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class BrewingClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_BARREL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HOP_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEAD_BLOCK, RenderLayer.getCutout());

        ScreenRegistry.register(ModScreenHandlers.OAK_BARREL_SCREEN_HANDLER, OakBarrelScreen::new);
    }
}
