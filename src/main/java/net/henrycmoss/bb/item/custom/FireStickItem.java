package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireStickItem extends Item {

    public FireStickItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()) {
            Vec3 dir = ShootingTools.shootFromRotation(pPlayer.getXRot(), pPlayer.getYRot(), 0f, 0.5f);
            pLevel.addAlwaysVisibleParticle(ParticleTypes.POOF, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    0d, 0d, 0d);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
