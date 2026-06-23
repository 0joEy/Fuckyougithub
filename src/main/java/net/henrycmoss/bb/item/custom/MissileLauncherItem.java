package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.entity.BbEntities;
import net.henrycmoss.bb.entity.custom.projectile.Missile;
import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MissileLauncherItem extends Item {

    public MissileLauncherItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(!level.isClientSide()) {
            float xRot = player.getXRot();
            float yRot = player.getYRot();
            Missile missile = BbEntities.MISSILE.get().create(level);
            missile.moveTo(player.position());
            missile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2f, 0.2f);
            level.addFreshEntity(missile);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.consume(stack);
    }
}
