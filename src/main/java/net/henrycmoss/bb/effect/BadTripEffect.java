package net.henrycmoss.bb.effect;

import net.henrycmoss.bb.Bb;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;

public class BadTripEffect extends MobEffect {

    protected BadTripEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    private int tick;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if(!level.isClientSide() && entity instanceof Player player) {
            if(tick >= 100) {
                Slime slime = new Slime(EntityType.SLIME, level);
                slime.setPos(entity.position());
                slime.setSize(4, true);
                slime.addTag("testBoss");
                level.addFreshEntity(slime);
                tick = 0;
            }
            else if(new Random().nextInt(0, 4) >= 3) tick++; entity.sendSystemMessage(Component.literal(Integer.toString(tick)));
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
