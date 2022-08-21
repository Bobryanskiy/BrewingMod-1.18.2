package net.bobr.brewingmod.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent MEAD = new FoodComponent.Builder().hunger(6).saturationModifier(0.1f).
            alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 5), 1).build();
    public static final FoodComponent BEER = new FoodComponent.Builder().hunger(6).saturationModifier(0.4f).
            alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 7), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 250, 0), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 650, 0), 1).build();
}
