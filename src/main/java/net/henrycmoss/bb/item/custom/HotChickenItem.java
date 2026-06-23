package net.henrycmoss.bb.item.custom;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.capabilities.BbCapabilities;
import net.henrycmoss.bb.capabilities.IExplosion;
import net.henrycmoss.bb.phys.BbMath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class HotChickenItem extends Item {

    private int lastCheckElapsed;
    private final int untilNextCheck = 10;

    private LivingEntity target;

    public HotChickenItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if(pLivingEntity instanceof Player player) {
            if(this.target != null) {
                if (player.isOnFire()) {
                    fireAtTarget(pLevel, player, this.target.position());
                }
                else {
                    this.target.getCapability(BbCapabilities.EXPLOSION).ifPresent(IExplosion::start);
                }
            }
            else fireLook(pLevel, player);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack pStack, int pRemainingUseDuration) {
        LogUtils.getLogger().info(lastCheckElapsed + "");
        //Selecting Entity
        if(++lastCheckElapsed >= untilNextCheck) {
            Vec3 start = player.getEyePosition();
            Vec3 dir = player.getLookAngle();
            double distance = 20;

            Vec3 end = dir.scale(distance);

            AABB bound = player.getBoundingBox()
                    .expandTowards(end)
                    .inflate(1d);
            EntityHitResult result = ProjectileUtil
                    .getEntityHitResult(player, start, end, bound,
                            entity -> !entity.isSpectator() && entity instanceof LivingEntity,
                            distance * distance);

            if(result != null) {
                LogUtils.getLogger().info("ee");
                if(this.target != result.getEntity()) {
                    this.target = (LivingEntity) result.getEntity();
                    this.target.addEffect(new MobEffectInstance(MobEffects.GLOWING,
                            750, 0, true, false));
                }
            }
            lastCheckElapsed = 0;
        }
        super.onUseTick(level, player, pStack, pRemainingUseDuration);
    }

    public void fireLook(Level level, Player player) {
        Random rand = new Random();
        double x1 = rand.nextDouble(0d, 30d);
        double x2 = rand.nextDouble(0d, 3d);
        int explosionPower = (int)(5 + (x1 / 6) + (12 / (1 + Math.pow(Math.E, -0.7 * (x1 - 26)))));
        double velocity = BbMath.gaussian(x2, 2d, 0.35d);
        Fireball fireball = EntityType.FIREBALL.create(level);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ExplosionPower", explosionPower);
        fireball.deserializeNBT(nbt);
        fireball.moveTo(player.position().add(0d, 1d, 0d).add(player.getLookAngle()));
        Vec3 power = player.getLookAngle().scale(velocity);
        fireball.xPower = power.x;
        fireball.yPower = power.y;
        fireball.zPower = power.z;
        level.addFreshEntity(fireball);
    }

    public void fireAtTarget(Level level, LivingEntity player, Vec3 pos) {
        Vec3 start = player.getLookAngle();
        double velocity = start.subtract(pos).length() / 5d;
        Fireball fireball = EntityType.FIREBALL.create(level);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("ExplosionPower", 25);
        fireball.deserializeNBT(nbt);
        fireball.moveTo(start);
        Vec3 power = start.scale(velocity);
        fireball.xPower = power.x;
        fireball.yPower = power.y;
        fireball.zPower = power.z;
        level.addFreshEntity(fireball);
    }

    class ExplosionData {
        int remaining;
        int duration;

        LivingEntity target;

        public int getTicksRemaining() {
            return remaining;
        }

        public void setTicksRemaining(int ticks) {
            this.remaining = ticks;
        }

        public void decrement() {
            if (remaining > 0) {
                remaining--;
            }
        }

        public LivingEntity getTarget() {
            return target;
        }
    }
}
