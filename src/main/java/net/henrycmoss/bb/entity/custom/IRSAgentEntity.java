package net.henrycmoss.bb.entity.custom;

import net.henrycmoss.bb.entity.ai.goal.TaxGoal;
import net.henrycmoss.bb.entity.client.IRSAgentModel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class IRSAgentEntity extends PathfinderMob implements NeutralMob {

    private boolean taken = false;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;


    public IRSAgentEntity(EntityType<? extends IRSAgentEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32d);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TaxGoal(this, 1d,
                true));
        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 0.9D, 32f));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,
                false, this::isAngryAt));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.ATTACK_DAMAGE, 2.5d)
                .add(Attributes.ATTACK_SPEED, 1.5d);
    }

    @Override
    public void tick() {
        super.tick();
        taken = this.hasItemInSlot(EquipmentSlot.MAINHAND);
    }

    @Override
    public void aiStep() {
        if(!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) level(), true);
        }
        super.aiStep();
    }

    public boolean taken() { return taken; }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof Player player) {
        }

        return super.hurt(pSource, pAmount);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
}
