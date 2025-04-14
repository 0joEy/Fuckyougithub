package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TntCannonItem extends Item {
    public TntCannonItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            PrimedTnt tnt = new PrimedTnt(EntityType.TNT, pLevel);
            tnt.setPos(pPlayer.position().add(0, 0.75, 0));
            tnt.setFuse(80);
            tnt.setDeltaMovement(ShootingTools.shootFromRotation(pPlayer.getXRot(), pPlayer.getYRot(), 0f,
                    1.5f));
            tnt.addTag("cannon");
            pLevel.addFreshEntity(tnt);
            for (String tag : tnt.getTags()) {
                pPlayer.sendSystemMessage(Component.literal(tag));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


}
