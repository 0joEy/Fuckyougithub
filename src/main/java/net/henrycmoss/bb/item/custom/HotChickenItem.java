package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.phys.BbMath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class HotChickenItem extends Item {

    public HotChickenItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if(pLivingEntity instanceof Player player) {
            Random rand = new Random();
            double x1 = rand.nextDouble(0d, 30d);
            double x2 = rand.nextDouble(0d, 3d);
            int explosionPower = (int)(5 + (x1 / 6) + (12 / (1 + Math.pow(Math.E, -0.7 * (x1 - 26)))));
            double velocity = BbMath.gaussian(x2, 2d, 0.35d);
            Vec3 direction = player.getLookAngle();
            Fireball fireball = EntityType.FIREBALL.create(pLevel);
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("ExplosionPower", explosionPower);
            fireball.deserializeNBT(nbt);
            fireball.moveTo(player.position().add(0d, 1d, 0d).add(direction));
            Vec3 power = direction.scale(velocity);
            fireball.xPower = power.x;
            fireball.yPower = power.y;
            fireball.zPower = power.z;
            pLevel.addFreshEntity(fireball);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
