package net.bobr.brewingmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobr.brewingmod.BrewingMod;
import net.bobr.brewingmod.block.enums.LiquidType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class OakBarrelScreen extends HandledScreen<OakBarrelScreenHandler> implements RecipeBookProvider {
    private static final Identifier TEXTURE =
            new Identifier(BrewingMod.MOD_ID, "textures/gui/oak_barrel_gui.png");
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
    private static final Identifier BOOK_BUTTON_TEXTURE = new Identifier(BrewingMod.MOD_ID, "textures/gui/mod_recipe_book.png");
    private static final Text TOGGLE_BREWING_RECIPES_TEXT = new TranslatableText("gui.recipebook.toggleRecipes.brewable");
    List<Text> tooltip = new ArrayList<>();
    private final List<OakBarrelButtonWidget> buttons = Lists.newArrayList();
    private final RecipeBookWidget recipeBook = new RecipeBookWidget() {
        @Override
        protected void setBookButtonTexture() {
            this.toggleCraftableButton.setTextureUV(0, 0, 28, 18, BOOK_BUTTON_TEXTURE);
        }

        @Override
        public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
            ItemStack itemStack = recipe.getOutput();
            this.ghostSlots.setRecipe(recipe);
            this.alignRecipeToGrid(this.craftingScreenHandler.getCraftingWidth(), this.craftingScreenHandler.getCraftingHeight(), this.craftingScreenHandler.getCraftingResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
        }

        @Override
        protected Text getToggleCraftableButtonText() {
            return TOGGLE_BREWING_RECIPES_TEXT;
        }
    };
    private boolean narrow;
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
        titleX = 8;
        TranslatableText uncork = new TranslatableText("text.oak_barrel.uncork");
        int size = textRenderer.getWidth(uncork) + 8;
        this.buttons.clear();
        this.narrow = this.width < 379;
        this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, (AbstractRecipeScreenHandler)this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.addButton(new CorkWidget(this.x + (58 - size - 1) / 2 + 4,(this.height - this.backgroundHeight) / 2 + 21, size, 20, new TranslatableText("text.oak_barrel.cork")));
        this.addButton(new UncorkWidget(this.x + (58 - size - 1) / 2 + 4, (this.height - this.backgroundHeight) / 2 + 45, size, 20, uncork));
        this.addDrawableChild(new TexturedButtonWidget(this.x + 143, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, button -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            this.buttons.forEach(button2 -> button2.setPos(this.x + (58 - size - 1) / 2 + 4, button2.getY()));
            ((TexturedButtonWidget)button).setPos(this.x + 143, this.height / 2 - 49);
        }));
        this.setInitialFocus(this.recipeBook);
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
        this.recipeBook.update();
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
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int k = 115;
        int l = 7;
        if (this.liquidType != null && this.liquidType != LiquidType.NOTHING) {
            if (this.liquidType.equals(LiquidType.WATER)) {
                switch (this.level) {
                    case 1 -> {
                        k = 185;l = 73;
                    }
                    case 2 -> {
                        k = 203;l = 73;
                    }
                    case 3 -> {
                        k = 221;l = 73;
                    }
                    case 4 -> {
                        k = 185;l = 0;
                    }
                    case 5 -> {
                        k = 203;l = 0;
                    }
                    case 6 -> {
                        k = 221;l = 0;
                    }
                }
            } else {
                switch (this.level) {
                    case 1 -> {
                        k = 131;l = 166;
                    }
                    case 2 -> {
                        k = 149;l = 166;
                    }
                    case 3 -> {
                        k = 167;l = 166;
                    }
                    case 4 -> {
                        k = 185;l = 146;
                    }
                    case 5 -> {
                        k = 203;l = 146;
                    }
                    case 6 -> {
                        k = 221;l = 146;
                    }
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

        if (this.recipeBook.isOpen() && this.narrow) {
            this.drawBackground(matrices, delta, mouseX, mouseY);
            this.recipeBook.render(matrices, mouseX, mouseY, delta);
        } else {
            this.recipeBook.render(matrices, mouseX, mouseY, delta);
            super.render(matrices, mouseX, mouseY, delta);
            this.recipeBook.drawGhostSlots(matrices, this.x, this.y, false, delta);
        }
        this.recipeBook.drawTooltip(matrices, this.x, this.y, mouseX, mouseY);
    }

    @Override
    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return (!this.narrow || !this.recipeBook.isOpen()) && super.isPointWithinBounds(x, y, width, height, pointX, pointY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBook);
            return true;
        }
        if (this.narrow && this.recipeBook.isOpen()) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
        this.recipeBook.slotClicked(slot);
    }

    @Override
    public void refreshRecipeBook() {
        this.recipeBook.refresh();
    }

    @Override
    public void removed() {
        this.recipeBook.close();
        super.removed();
    }

    @Override
    public RecipeBookWidget getRecipeBookWidget() {
        return this.recipeBook;
    }

    @Environment(value=EnvType.CLIENT)
    interface OakBarrelButtonWidget {
        boolean shouldRenderTooltip();

        void renderTooltip(MatrixStack var1, int var2, int var3);

        void tick(boolean var1);

        void setPos(int x, int y);

        int getY();
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
        public void setPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getY() {
            return this.y;
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
