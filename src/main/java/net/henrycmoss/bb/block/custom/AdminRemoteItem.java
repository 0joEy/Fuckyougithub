package net.henrycmoss.bb.block.custom;

import net.henrycmoss.bb.block.BbBlocks;
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
            KeyCardReaderBlock readerBlock = (KeyCardReaderBlock) state.getBlock();
            readerBlock.cycleLevel(state, level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
}
