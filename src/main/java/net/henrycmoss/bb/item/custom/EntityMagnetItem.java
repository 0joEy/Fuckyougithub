package net.henrycmoss.bb.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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

    public EntityMagnetItem(Item.Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if(!level.isClientSide()) {
            BlockHitResult result = level.clip(new ClipContext(player.getEyePosition(),
                    player.getEyePosition().add(player.getLookAngle().normalize().scale(20)),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if(result.getType() == HitResult.Type.BLOCK) {
                attractionCenter = result.getBlockPos();
                List<Entity> selected = level.getEntities(player, new AABB(attractionCenter).inflate(50),
                        entity -> entity instanceof LivingEntity);
                for (Entity e : selected) {
                    if (e instanceof LivingEntity target) {
                        Vec3 start = target.position().add(0, 50d, 0);
                        BlockHitResult hit = level.clip(
                                new ClipContext(start, Vec3.atCenterOf(attractionCenter), ClipContext.Block.COLLIDER,
                                        ClipContext.Fluid.NONE, null));
                        if(hit.getType() == HitResult.Type.ENTITY || hit.getType() == HitResult.Type.MISS) {
                            targets.add(target);
                        }
                    }
                }
                player.startUsingItem(pUsedHand);
            }
            return InteractionResultHolder.consume(player.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(pUsedHand));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if(!pLevel.isClientSide()) {
            for(LivingEntity target : targets) {
                Vec3 distance = Vec3.atCenterOf(attractionCenter).subtract(target.position());
                if(distance.length() <= 10d) targets.remove(target);
                target.addDeltaMovement(distance.normalize().scale(0.2f));
            }
        }
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
