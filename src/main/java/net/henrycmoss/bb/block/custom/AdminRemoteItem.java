package net.henrycmoss.bb.block.custom;

import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.entity.KeyCardReaderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdminRemoteItem extends Item {

    public AdminRemoteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if(state.is(BbBlocks.KEY_CARD_READER.get())) {
            if(level.getBlockEntity(pos) instanceof KeyCardReaderBlockEntity be) be.cycle(level, pos, state);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
}
