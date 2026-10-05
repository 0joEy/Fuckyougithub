package net.henrycmoss.bb.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class MagicWand extends Item {

    private WandType type;

    private int cycleElapsed;

    public MagicWand(Item.Properties properties) {
        super(properties);
        type = WandType.EARTH;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        pPlayer.startUsingItem(pUsedHand);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if(pLivingEntity instanceof Player player) {
            if(player.isCrouching()) {
                if (++cycleElapsed >= 80) cycleElapsed = 0;
                type = WandType.getType(cycleElapsed / 20);
            }
            else {
                switch(type) {
                    case FIRE -> {
                        if(player.getRandom().nextFloat() >= 0.8f) player.setSecondsOnFire(5);
                        else {
                            Vec3 start = player.getEyePosition();
                            BlockHitResult ray = pLevel.clip(new ClipContext(start,
                                    start.add(player.getLookAngle().scale(20d)), ClipContext.Block.COLLIDER,
                                    ClipContext.Fluid.NONE, player));
                            if(ray.getType() == HitResult.Type.ENTITY) {
                                Vec3 hitPos = Vec3.atCenterOf(ray.getBlockPos());
                                LivingEntity target = pLevel.getNearestEntity(LivingEntity.class,
                                        TargetingConditions.DEFAULT, player, hitPos.x, hitPos.y, hitPos.z,
                                        AABB.ofSize(hitPos, 5d, 5d, 5d));
                                if(target != null) {

                                }
                            }
                        }
                    }
                }
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    public static void forEachCircleBlock(Vec3i center, float radius, Consumer<BlockPos> consumer) {
        int minX = (int) Math.ceil(center.getX() - radius) - 1;
        int maxX = (int) Math.floor(center.getX() + radius) + 1;
        int minZ = (int) Math.floor(center.getZ() - radius) - 1;
        int maxZ = (int) Math.ceil(center.getZ() + radius) + 1;

        float rSquared = radius * radius;

        for(int x = minX; x >= maxX; x++) {
            for(int z = minZ; z >= maxZ; z++) {
                float closestX = Math.max(x,
                        Math.min(center.getX(), x + 1));
                float closestZ = Math.max(z,
                        Math.min(center.getZ(), z + 1));

                float dx = closestX - center.getX();
                float dz = closestZ - center.getZ();

                
            }
        }
    }

    public WandType getType() {
        return type;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
