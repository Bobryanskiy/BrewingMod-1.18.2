package net.bobr.testmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobr.testmod.TestMod;
import net.bobr.testmod.block.custom.OakBarrelBlock;
import net.bobr.testmod.block.entity.OakBarrelBlockEntity;
import net.bobr.testmod.block.enums.LiquidType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OakBarrelScreen extends HandledScreen<OakBarrelScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(TestMod.MOD_ID, "textures/gui/oak_barrel_gui.png");
    List<Text> tooltip = new ArrayList<>();
    private final List<OakBarrelButtonWidget> buttons = Lists.newArrayList();
    LiquidType liquidType;
    int level;
    private boolean cork;

    public OakBarrelScreen(final OakBarrelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler2, int slotId, ItemStack stack) {
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler2, int property, int value) {
                OakBarrelScreen.this.liquidType = handler.getLiquidType();
                OakBarrelScreen.this.level = handler.getLevel();
                OakBarrelScreen.this.cork = handler.getCork();
                OakBarrelScreen.this.tooltipUpdate();
            }
        });
    }

    private void tooltipUpdate() {
        LiquidType d = this.liquidType;
        List<Text> temp = new ArrayList<>();
        if (d != LiquidType.NOTHING) {
            String s = d.toString().toUpperCase().charAt(0) + d.toString().substring(1);
            String s1 = this.level + " / 6";
            temp.add(Text.of(s));
            temp.add(Text.of(s1));
            this.tooltip.clear();
            this.tooltip.addAll(temp);
        }
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add((OakBarrelButtonWidget) button);
    }

    @Override
    protected void init() {
        super.init();
        titleX = 8;//(backgroundWidth - textRenderer.getWidth(title)) / 2;
        TranslatableText uncork = new TranslatableText("text.oak_barrel.uncork");
        int size = textRenderer.getWidth(uncork) + 8;
        this.buttons.clear();
        this.addButton(new CorkWidget((this.width - this.backgroundWidth) / 2 + (58 - size - 1) / 2 + 4,(this.height - this.backgroundHeight) / 2 + 21, size, 20, new TranslatableText("text.oak_barrel.cork")));
        this.addButton(new UncorkWidget((this.width - this.backgroundWidth) / 2 + (58 - size - 1) / 2 + 4, (this.height - this.backgroundHeight) / 2 + 45, size, 20, uncork));
    }

    protected void renderTooltip(MatrixStack matrices, int x, int y) {
        if (!this.tooltip.isEmpty()) {
            this.renderTooltip(matrices, this.tooltip, x, y);
        }
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        boolean cork = this.cork;
        this.buttons.forEach(button -> button.tick(cork));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int k = 115;
        int l = 7;
        if (this.liquidType != null && this.liquidType != LiquidType.NOTHING) {
            if (this.liquidType.equals(LiquidType.WATER)) {
                switch (this.level) {
                    case 1: {k = 185;l = 73;break;}
                    case 2: {k = 203;l = 73;break;}
                    case 3: {k = 221;l = 73;break;}
                    case 4: {k = 185;l = 0;break;}
                    case 5: {k = 203;l = 0;break;}
                    case 6: {k = 221;l = 0;}
                }
            } else {
                switch (this.level) {
                    case 1: {k = 131;l = 166;break;}
                    case 2: {k = 149;l = 166;break;}
                    case 3: {k = 167;l = 166;break;}
                    case 4: {k = 185;l = 146;break;}
                    case 5: {k = 203;l = 146;break;}
                    case 6: {k = 221;l = 146;}
                }
            }
        }
        this.drawTexture(matrices, i + 115, j + 7, k, l, 18, 73);

        if (handler.isCrafting()) {
            this.drawTexture(matrices, i + 101, j + 22, 176, 0, 9, this.handler.getScaledProgress());
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);

        if (mouseX >= this.x + 115 && mouseY >= this.y + 7 && mouseX <= this.x + 132
                && mouseY <= this.y + 79) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @Environment(value=EnvType.CLIENT)
    static interface OakBarrelButtonWidget {
        public boolean shouldRenderTooltip();

        public void renderTooltip(MatrixStack var1, int var2, int var3);

        public void tick(boolean var1);
    }

    @Environment(value=EnvType.CLIENT)
    class CorkWidget extends BaseButtonWidget {
        public CorkWidget(int i, int j, int k, int l, Text text) {
            super(i, j, k, l, text);
        }

        @Override
        public void tick(boolean var1) {
            this.active = !var1;
        }

        @Override
        public void onPress() {
            OakBarrelScreen.this.client.interactionManager.clickButton(OakBarrelScreen.this.handler.syncId, 1);
        }
    }

    @Environment(value=EnvType.CLIENT)
    class UncorkWidget extends BaseButtonWidget {

        public UncorkWidget(int i, int j, int k, int l, Text text) {
            super(i, j, k, l, text);
        }

        @Override
        public void tick(boolean var1) {
            this.active = var1;
        }

        @Override
        public void onPress() {
            OakBarrelScreen.this.client.interactionManager.clickButton(OakBarrelScreen.this.handler.syncId, 2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    static abstract class BaseButtonWidget extends PressableWidget implements OakBarrelButtonWidget {

        public BaseButtonWidget(int i, int j, int k, int l, Text text) {
            super(i, j, k, l, text);
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            TextRenderer textRenderer = minecraftClient.textRenderer;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            int i = this.getYImage(this.isHovered());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
            int j = this.active ? 0xFFFFFF : 0xA0A0A0;
            ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
        }

        @Override
        public boolean shouldRenderTooltip() {
            return false;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }
}
