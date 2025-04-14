package net.henrycmoss.bb.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class JointItem extends Item {

    public JointItem(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.PLAYER_BREATH;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
}
