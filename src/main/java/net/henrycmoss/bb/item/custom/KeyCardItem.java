package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.KeyCardReaderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class KeyCardItem extends Item {
    private final Authorizations level;

    public KeyCardItem(Properties properties, Authorizations level) {
        super(properties);
        this.level = level;
    }

    public Authorizations get() {
        return level;
    }
}
