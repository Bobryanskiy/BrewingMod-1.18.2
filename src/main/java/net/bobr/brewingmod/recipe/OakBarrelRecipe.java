package net.bobr.brewingmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.bobr.brewingmod.block.ModBlocks;
import net.bobr.brewingmod.item.ModItems;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class OakBarrelRecipe implements Recipe<SimpleInventory> {
    private final String group;
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public OakBarrelRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;
        for (int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.isEmpty()) continue;
            ++i;
            recipeMatcher.addInput(itemStack,1);
        }
        return i == this.recipeItems.size() && recipeMatcher.match(this, null);
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= this.recipeItems.size();
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.OAK_BARREL);
    }

    public static class Type implements RecipeType<OakBarrelRecipe> {
        private Type() {
        }
        public static final Type INSTANCE = new Type();
        public static final String ID = "oak_barrel";
    }

    public static class Serializer implements RecipeSerializer<OakBarrelRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "oak_barrel";

        @Override
        public OakBarrelRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> inputs = Serializer.getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (inputs.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (inputs.size() > 6) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new OakBarrelRecipe(identifier, string, result, inputs);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        @Override
        public OakBarrelRecipe read(Identifier id, PacketByteBuf buf) {
            String string = buf.readString();
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new OakBarrelRecipe(id, string, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, OakBarrelRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeInt(recipe.recipeItems.size());
            for (Ingredient ing : recipe.recipeItems) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.output);
        }
    }
}
