package net.henrycmoss.bb.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcidBurnEffect extends MobEffect {

    protected AcidBurnEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    private int tick = 0;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        amplifier += 1;
        int ticks = amplifier > 1 ? 20 / amplifier : 20;
        if(tick > ticks) {
            entity.hurt(entity.damageSources().magic(), amplifier > 1 ? amplifier * 1.25f : 1f);
            tick = 0;
        }
        tick++;
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
