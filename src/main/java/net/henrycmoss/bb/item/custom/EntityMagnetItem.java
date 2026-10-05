package net.henrycmoss.bb.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityMagnetItem extends Item {

    private final List<LivingEntity> targets = new ArrayList<>();
    private BlockPos attractionCenter;
    private int sinceLastCheck;

    private static final Vec3 LEFT = new Vec3(-1, 0, 0);
    private static final Vec3 RIGHT = new Vec3(1, 0, 0);
    private static final Vec3 UP = new Vec3(0, 1, 0);
    private static final Vec3 DOWN = new Vec3(0, -1, 0);

    public EntityMagnetItem(Item.Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if(!level.isClientSide()) {

            BlockHitResult blockHitResult = level.clip(new ClipContext(player.getEyePosition(),
                    player.getEyePosition().add(player.getLookAngle().normalize().scale(20d)),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

            if(blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = blockHitResult.getBlockPos();
                level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
                attractionCenter = pos;
            }
            targets.addAll(level.getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(100)));
        }
        return InteractionResultHolder.fail(player.getItemInHand(pUsedHand));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if(!pLevel.isClientSide()) {
            for (LivingEntity target : targets) {
                pLivingEntity.sendSystemMessage(Component.literal(target.getMobType().toString()));
                Vec3 center = Vec3.atCenterOf(attractionCenter);
                Vec3 toCenter = center.subtract(target.position());

                Vec3 initialMovement = toCenter.normalize().scale(2);
                Vec3 actualMovement = initialMovement;
                if (++sinceLastCheck >= 2) {
                    actualMovement = Entity.collideBoundingBox(target, initialMovement,
                            target.getBoundingBox(), pLevel, List.of());

                    if (actualMovement.length() < initialMovement.length()) {
                        Vec3[] candidates = {
                                initialMovement.add(LEFT.scale(0.5d)),
                                initialMovement.add(RIGHT.scale(0.5d)),
                                initialMovement.add(UP.scale(0.5d)),
                                initialMovement.add(DOWN.scale(0.5d))
                        };
                        for (Vec3 candidate : candidates) {
                            Vec3 movement = Entity.collideBoundingBox(target, candidate,
                                    target.getBoundingBox(), pLevel, List.of());
                            if (movement.length() >= initialMovement.length()) {
                                actualMovement = movement;
                                target.moveTo(initialMovement.scale(5d));
                                break;
                            }
                        }
                    }
                    sinceLastCheck = 0;
                }
                target.setDeltaMovement(actualMovement);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BRUSH;
    }
}
