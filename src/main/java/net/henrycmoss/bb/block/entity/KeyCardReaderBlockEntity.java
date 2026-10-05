package net.henrycmoss.bb.block.entity;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.block.custom.KeyCardReaderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class KeyCardReaderBlockEntity extends BlockEntity {

    private int auth;
    private int elapsed;
    private int poweredElapsed;

    private final int flashInterval = 15;
    private final int poweredTime = 20;

    private boolean powered;
    private boolean isFlashing;

    public KeyCardReaderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BbBlockEntities.KEY_CARD_READER_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("level", auth);
        pTag.putInt("elapsedFlashTime", elapsed);
        if(powered) pTag.putInt("sincePowered", poweredElapsed);
        pTag.putBoolean("powered", powered);
        pTag.putBoolean("flashing", isFlashing);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        auth = pTag.getInt("level");
        poweredElapsed = pTag.getInt("sincePowered");
        elapsed = pTag.getInt("elapsedFlashTime");
        powered = pTag.getBoolean("powered");
        isFlashing = pTag.getBoolean("flashing");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        LogUtils.getLogger().info("powered: " + powered + ", state: "
         + state.getValue(KeyCardReaderBlock.POWERED));
        if (powered) {
            if(++poweredElapsed >= poweredTime) {
                LogUtils.getLogger().info("time: " + poweredElapsed);
                powered = false;
                reset(level, pos, state);
            }
        } else if (++elapsed >= flashInterval) {
            isFlashing = !isFlashing;
            level.setBlock(pos, state.setValue(KeyCardReaderBlock.FLASHING, isFlashing), 3);
            elapsed = 0;
        }
    }

    public void reset(Level level, BlockPos pos, BlockState state) {
        elapsed = 0;
        poweredElapsed = 0;
        isFlashing = false;
        BlockState current = state;
        current = current.setValue(KeyCardReaderBlock.POWERED, false);
        current = current.setValue(KeyCardReaderBlock.FLASHING, false);
        level.setBlock(pos, current, 3);
    }

    public void power(Level level, BlockPos pos, BlockState state) {
        powered = true;
        level.setBlock(pos, state.setValue(KeyCardReaderBlock.POWERED, true), 3);
    }

    public void cycle(Level level, BlockPos pos, BlockState state) {
        auth = auth + 1 > 4 ? 0 : auth + 1;
        level.setBlock(pos, state.setValue(KeyCardReaderBlock.LEVEL, auth), 3);
    }

}
