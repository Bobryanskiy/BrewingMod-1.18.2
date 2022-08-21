package net.bobr.brewingmod.fabricasm;

import com.chocohead.mm.api.ClassTinkerers;
import com.mojang.datafixers.util.Pair;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.IOException;
import java.util.Map;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        ClassTinkerers.enumBuilder("net/minecraft/recipe/book/RecipeBookCategory")
                .addEnum("OAK_BARREL").build();

        ClassTinkerers.addTransformation("net/minecraft/recipe/book/RecipeBookOptions", classNode -> {
            try {
                classNode = new ClassNode();
                ClassReader classReader = new ClassReader("net/minecraft/recipe/book/RecipeBookOptions");
                classReader.accept(classNode, 0);
                for (FieldNode fieldNode : classNode.fields) {
                    if (fieldNode.name.equals("CATEGORY_OPTION_NAMES")) {
                        Map<RecipeBookCategory, Pair<String, String>> map = (Map<RecipeBookCategory, Pair<String, String>>) fieldNode.value;

                        map.put(ClassTinkerers.getEnum(RecipeBookCategory.class, "OAK_BARREL"), Pair.of("isOakBarrelGuiOpen", "isOakBarrelFilteringCraftable"));

                        classNode.fields.set(classNode.fields.indexOf(fieldNode), new FieldNode(fieldNode.access, fieldNode.name, Type.getDescriptor(RecipeBookOptions.class), fieldNode.signature, map));
                        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        classNode.accept(classWriter);
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
