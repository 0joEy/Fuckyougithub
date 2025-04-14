package net.henrycmoss.bb.effect;

import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Random;

public class HallucinationEffect extends MobEffect {

    private int tick = 0;
    private static LivingEntity haunter = null;



    public HallucinationEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if(!entity.level().isClientSide()) {
            if(!(getHaunter() == null) && getHaunter() instanceof Monster) {
                getHaunter().setSpeed(0.5f);
                entity.sendSystemMessage(Component.literal("default: " + getHaunter().getName()));
                if(tick >= 20) {
                    getHaunter().setPos(entity.position());
                    entity.sendSystemMessage(getHaunter().getName());
                    getHaunter().setDeltaMovement(ShootingTools.shootFromRotation(entity.getXRot(), entity.getYRot(), 0, 2));
                    tick = 0;
                }
                else if(new Random().nextInt(0, 5) >= 4) tick++;
            }
            else {
                tick = 0;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    public static LivingEntity getHaunter() {
        return haunter;
    }

    public static void setHaunter(@Nullable LivingEntity entity) {
        haunter = entity;
    }
}
