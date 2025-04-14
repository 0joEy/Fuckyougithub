package net.henrycmoss.bb.food;

import net.henrycmoss.bb.effect.BbEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class BbFoods {

    public static final FoodProperties JOINT = (new FoodProperties.Builder()).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 5000, 2), 1)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1500, 7), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3500, 0), 0.87f)
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 2500, 1), 0.75f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000, 1), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3000, 2), 0.75f)
            .build();

    public static final FoodProperties MAGIC_MUSHROOMS = (new FoodProperties.Builder()).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 8000, 1), 1f)
            .effect(() -> new MobEffectInstance(BbEffects.SHROOMS.get(), 8000), 1.0f).build();

    public static final FoodProperties METHAMPHETAMINE = (new FoodProperties.Builder()).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 2000, 0), 1)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1250, 1), 0.75f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 500, 0), 0.65f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 0.25f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 750, 1), 0.25f)
            .effect(() -> new MobEffectInstance(MobEffects.WITHER, 175, 2), 0.05f)
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0), 0.7f)
            .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 1000, 1), 0.9f).build();

    public static final FoodProperties ALCOHOL = (new FoodProperties.Builder()).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 1500, 1), 0.15f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 5000, 0), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 2500, 0), 0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3500, 0), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 4500), 0.5f).build();

    public static final FoodProperties LSD_TABLET = (new FoodProperties.Builder()).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 1500, 0), 0.85f)
            .effect(() -> new MobEffectInstance(BbEffects.HALLUCINATION.get(), 8000), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8000), 0.95f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 8000), 1f).build();
}
