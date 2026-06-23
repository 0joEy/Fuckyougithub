package net.henrycmoss.bb.entity.custom;

import net.henrycmoss.bb.entity.BbEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EvilPigEntity extends Animal {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public EvilPigEntity(EntityType<? extends EvilPigEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return BbEntities.EVIL_PIG.get().create(level());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0,
                new MeleeAttackGoal(this, 0.5d, false));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                Player.class, true));
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                Pig.class, true));
        this.goalSelector.addGoal(3, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.ATTACK_DAMAGE, 5d)
                .add(Attributes.ARMOR, 10d)
                .add(Attributes.ATTACK_SPEED, 5d)
                .add(Attributes.MOVEMENT_SPEED, 0.5d);
    }
}
