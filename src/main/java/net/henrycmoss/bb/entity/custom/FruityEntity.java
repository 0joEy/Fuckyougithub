package net.henrycmoss.bb.entity.custom;

import net.henrycmoss.bb.util.BbTags;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class FruityEntity extends TamableAnimal implements NeutralMob {
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(FruityEntity.class, EntityDataSerializers.INT);
    public static final Predicate<LivingEntity> PREY_PREDICATE = prey -> prey instanceof Animal || prey instanceof Skeleton;

    private final List<ItemEntity>

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 59);
    @Nullable
    private UUID persistentAngerTarget;



    public FruityEntity(EntityType<? extends FruityEntity> type, Level level) {
        super(type, level);
        this.setTame(false);
        fruitTargets = new ArrayList<>();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GatherFruitGoal(this, 20f));
    }

    static class GatherFruitGoal extends Goal {

        private final FruityEntity mob;
        private final float distance;

        private final List<ItemEntity> fruitTargets;

        private Path path;
        private ItemEntity currentTarget;

        private int elapsed;
        private final int untilNext = 10;
        private int untilPathRecalculation;
        private int untilNextAttack;

        public GatherFruitGoal(FruityEntity mob, float distance) {
            this.mob = mob;
            this.distance = distance;
            fruitTargets = new ArrayList<>();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, 1);
            this.mob.setAggressive(true);
            this.untilPathRecalculation = 0;
            this.untilNextAttack = 0;
            super.start();
        }

        @Override
        public void stop() {
            if(!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(currentTarget)) this.mob.setTarget(null);
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            Level level = mob.level();
            ItemEntity fruit = fruitTargets.get(0);
            if(!level.isClientSide() && fruit != null) {
                this.mob.getLookControl().setLookAt(fruit);
                untilPathRecalculation = Math.max(untilPathRecalculation - 1, 0);
                double d0 = this.mob.distanceTo(fruit);
                if(d0 > 1d && untilPathRecalculation <= 0) {
                    untilPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (!this.mob.getNavigation().moveTo(fruit, 1)) {
                        untilPathRecalculation += 15;
                    }

                    untilPathRecalculation = this.adjustedTickDelay(this.untilPathRecalculation);
                }
                untilNextAttack = Math.max(untilNextAttack - 1, 0);
                checkAndPerformGather(currentTarget, d0);
            }
            super.tick();
        }

        @Override
        public boolean canUse() {
            Level level = mob.level();
            if(!level.isClientSide()) {
                if (++elapsed >= untilNext) {
                    AABB bound = new AABB(mob.blockPosition()).inflate(20f);
                    level.getEntities(mob, bound, entity -> entity instanceof ItemEntity item
                            && item.getItem().is(BbTags.Items.FRUITS)).forEach((item) -> fruitTargets.add(item.getUUID()));

                    if(!fruitTargets.isEmpty()) {
                        this.currentTarget = fruitTargets.get(0);
                        this.path = this.mob.getNavigation().createPath(currentTarget, 0);
                        untilPathRecalculation = 4 + mob.getRandom().nextInt(7);
                        return this.path != null;
                    }
                }
            }
            return !fruitTargets.isEmpty();
        }

        private void checkAndPerformGather(ItemEntity fruit, double distance) {
            if(distance <= 2d && fruit.distanceTo(this.mob) <= 1d && untilNextAttack <= 0) {
                fruitTargets.remove(fruit);
                this.mob.pickUpItem(fruit);
                untilNextAttack = 20;
            }
        }
    }
}
