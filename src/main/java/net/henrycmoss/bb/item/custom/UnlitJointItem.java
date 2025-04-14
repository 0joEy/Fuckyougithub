package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class UnlitJointItem extends Item {

    public UnlitJointItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(!pLevel.isClientSide) {
            ItemStack offhand = pPlayer.getOffhandItem();
            if(offhand.is(Items.FLINT_AND_STEEL)) {
                if(!pPlayer.isCreative()) {
                    ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
                    pPlayer.setItemInHand(pUsedHand, new ItemStack(itemInHand.getItem(),
                            itemInHand.getCount() - 1));
                    offhand.hurtAndBreak(1, pPlayer, (player -> player.broadcastBreakEvent(pUsedHand)));
                }
                if(pPlayer.getItemInHand(pUsedHand).getCount() > 1) {
                    if(!pPlayer.getInventory().add(new ItemStack(BbItems.JOINT.get(), 1))) {
                        ItemEntity airJoint = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY() + 1,
                                pPlayer.getZ(), new ItemStack(BbItems.JOINT.get()));
                        airJoint.setDeltaMovement(ShootingTools.shootFromRotation(pPlayer.getXRot(), pPlayer.getYRot(),
                                0f, 0.4f));
                        pLevel.addFreshEntity(airJoint);
                    }
                }
                else {
                    pPlayer.setItemInHand(pUsedHand, BbItems.JOINT.get().getDefaultInstance());
                }
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return InteractionResult.FAIL;
    }
}
