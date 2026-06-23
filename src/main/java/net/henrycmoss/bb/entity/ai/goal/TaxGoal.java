package net.henrycmoss.bb.entity.ai.goal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;

import java.util.ArrayList;
import java.util.List;

public class TaxGoal extends Goal {
    protected final PathfinderMob mob;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private final int attackInterval = 20;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public TaxGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
    }

    @Override
    public boolean canUse() {
        long i = this.mob.level().getGameTime();
        if(i - this.lastCanUseCheck < 20L) return false;
        else {
            this.lastCanUseCheck = i;
            Player target = (Player) this.mob.getTarget();
            if(target == null) return false;
            else if(!target.isAlive()) return false;
            else {
                if(canPenalize) {
                    if(--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.mob.getNavigation().createPath(target, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        return this.path != null;
                    }
                    else return true;
                }
                this.path = this.mob.getNavigation().createPath(target, 0);
                if(this.path != null) return true;
                else return this.getAttackReachSqr(target) >= this.mob.distanceToSqr(target);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        Player player = (Player) this.mob.getTarget();
        if (player == null) {
            return false;
        } else if (!player.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else if (!this.mob.isWithinRestriction(player.blockPosition())) {
            return false;
        } else {
            return !player.isSpectator() && !player.isCreative();
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(path, speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        Player player = (Player) this.mob.getTarget();
        if(player != null && ((player.isCreative() || player.isSpectator()) || !player.isAlive())) {
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        Player target = (Player) this.mob.getTarget();
        if(target != null) {
            this.mob.getLookControl().setLookAt(target, 30f, 30f);
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if((this.followingTargetEvenIfNotSeen || this.mob.hasLineOfSight(target))
                    && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D
                    && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D
                    || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D
                    || this.mob.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = target.getX();
                this.pathedTargetY = target.getY();
                this.pathedTargetZ = target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if (this.mob.getNavigation().getPath() != null) {
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && target.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.tax(target, d0);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth());
    }

    protected void tax(Player target, double distance) {
        if(distance <= this.getAttackReachSqr(target) && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            List<Integer> slots = new ArrayList<>();
            for(int i = 0; i < target.getInventory().getContainerSize(); i++) {
                if(!target.getSlot(i).get().isEmpty()) slots.add(i);
            }
            if(!slots.isEmpty()) {
                int slot = this.mob.getRandom().nextInt(slots.size());
                this.mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                this.mob.setItemInHand(InteractionHand.MAIN_HAND, target.getSlot(slot).get());
                target.getSlot(slot).set(ItemStack.EMPTY);
                target.sendSystemMessage(Component.literal("dd"));
            }
        }
    }
}
