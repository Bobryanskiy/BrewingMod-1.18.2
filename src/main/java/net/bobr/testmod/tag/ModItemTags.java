package net.bobr.testmod.tag;

import net.bobr.testmod.TestMod;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModItemTags {
    public static final TagKey<Item> OAK_BARREL_ITEMS = ModItemTags.register("oak_barrel_items");

    private ModItemTags() {
    }

    private static TagKey<Item> register(String id) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(TestMod.MOD_ID, id));
    }
}
