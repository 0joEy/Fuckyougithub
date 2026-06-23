package net.henrycmoss.bb.entity.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PoliceOfficerEntity extends PathfinderMob implements NeutralMob {

    private static final UniformInt PERSISTENT_ANGER_TIME = UniformInt.of(20, 39);
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;

    public PoliceOfficerEntity(EntityType<? extends PoliceOfficerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1,
                new DetainLittererGoal(this, 1.0D, false));
        this.goalSelector.addGoal(1,
                new MoveTowardsTargetGoal(this, 0.9D, 32f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 25d)
                .add(Attributes.MOVEMENT_SPEED, 0.35d)
                .add(Attributes.FOLLOW_RANGE, 32d);
    }

    @Override
    public void aiStep() {
        if(!this.level().isClientSide()) this.updatePersistentAnger((ServerLevel) this.level(),
                true);
        super.aiStep();
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    static class DetainLittererGoal extends Goal {

        protected final PoliceOfficerEntity officer;
        private ItemEntity litter;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private int heldInPlaceTicks;
        private final int holdTime = 30;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;
        private boolean cleaning = false;

        private final TargetingConditions predicate = TargetingConditions.forNonCombat()
                .selector((target) -> {
                    ServerLevel level = (ServerLevel) target.level();
                    if(target.getPersistentData().contains("litterUUID")) {
                        ItemEntity litter = (ItemEntity)
                                level.getEntity(target.getPersistentData().getUUID("litterUUID"));
                        if(litter != null && litter.getItem().getOrCreateTag().getBoolean("litter")) {
                            this.litter = litter;
                            return target.getPersistentData().contains("litterer")
                                    && litter.onGround();
                        }
                    }
                    return target instanceof Player && target.getPersistentData().getBoolean("litterer");
                });

        public DetainLittererGoal(PoliceOfficerEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            this.officer = mob;
            this.speedModifier = speedModifier;
            this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        }

        @Override
        public boolean canUse() {
            long i = this.officer.level().getGameTime();
            if(i - this.lastCanUseCheck < 20L) return false;
            else {
                this.lastCanUseCheck = i;
                Player offender = this.officer.level().getNearestPlayer(predicate, this.officer);
                Entity target;
                if(this.litter != null) {
                    target = this.litter;
                    cleaning = true;
                }
                else {
                    if(offender == null || !offender.isAlive()) return false;
                    target = offender;
                    this.officer.setTarget(offender);
                }

                this.path = this.officer.getNavigation().createPath(target, 0);
                if(this.path != null) return true;
                else return this.getAttackReachSqr(target) >= this.officer.distanceToSqr(target);
            }
        }

        @Override
        public boolean canContinueToUse() {
            Player player = (Player) this.officer.getTarget();
            if (player == null) {
                return false;
            } else if (!player.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.officer.getNavigation().isDone();
            } else if (!this.officer.isWithinRestriction(player.blockPosition())) {
                return false;
            }
            else if(!player.getPersistentData().contains("litterer")) return false;
            else {
                return !player.isSpectator() && !player.isCreative();
            }
        }

        @Override
        public void start() {
            this.officer.getNavigation().moveTo(path, speedModifier);
            this.officer.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            Player player = (Player) this.officer.getTarget();
            if(player == null) return;
            if(player.isCreative() || player.isSpectator()) {
                this.officer.setTarget(null);
            }

            this.officer.setAggressive(false);
            this.officer.getNavigation().stop();
        }

        @Override
        public void tick() {
            Entity target = this.cleaning ? this.litter : this.officer.getTarget();
            if(target != null) {
                double d0 = this.cleaning ? this.officer.distanceToSqr(this.litter) :
                        this.officer.getPerceivedTargetDistanceSquareForMeleeAttack((LivingEntity) target);
                this.officer.getLookControl().setLookAt(target, 30f, 30f);
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if((this.cleaning || this.followingTargetEvenIfNotSeen || this.officer.hasLineOfSight(target))
                        && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D
                        && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D
                        || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D
                        || this.officer.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = target.getX();
                    this.pathedTargetY = target.getY();
                    this.pathedTargetZ = target.getZ();
                    this.ticksUntilNextPathRecalculation = 2 + this.officer.getRandom().nextInt(7);

                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.officer.getNavigation().moveTo(target, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                if(this.cleaning) clean(d0);
                else if(target instanceof Player player) detainOffender(player, d0);
            }
        }

        protected double getAttackReachSqr(Entity pAttackTarget) {
            return (double)(this.officer.getBbWidth() * 2.0F * this.officer.getBbWidth() * 2.0F + pAttackTarget.getBbWidth());
        }

        protected void clean(double distance) {
            if(distance <= 2d) {
                this.officer.pickUpItem(this.litter);
                this.cleaning = false;
            }
        }

        protected void detainOffender(Player offender, double distance) {
            if(distance <= this.getAttackReachSqr(offender)) {
                LogUtils.getLogger().info("detaining");
            }
        }
    }
}
