package net.bobr.brewingmod.cient;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AlcoholHudOverlay implements HudRenderCallback {
    private static final Identifier FILLED_ALCOHOL = new Identifier(BrewingMod.MOD_ID, "textures/item/mead.png");
    private static final Identifier EMPTY_ALCOHOL = new Identifier(BrewingMod.MOD_ID, "textures/item/mug.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, FILLED_ALCOHOL);
        for (int i = 0; i < 10; i++) {
            float f = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getFloat("alcohol");
            if (f > i) {
                DrawableHelper.drawTexture(matrixStack, x - 94 + (i * 9), y - 54, 0, 0, (int) (12 * f), 12, 12, 12);
            }
        }
    }
}
