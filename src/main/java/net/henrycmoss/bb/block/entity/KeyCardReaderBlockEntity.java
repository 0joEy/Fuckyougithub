package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.block.custom.KeyCardReaderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.henrycmoss.bb.block.custom.KeyCardReaderBlock;

public class KeyCardReaderBlockEntity extends BlockEntity {

    private boolean flashing;
    private boolean powered;
    private int sinceLastInterval;

    private int elapsed;
    private final int flashInterval = 15;
    private final int powerDuration = 30;

    public KeyCardReaderBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.KEY_CARD_READER_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("flash", flashing);
        pTag.putBoolean("powered", powered);
        pTag.putInt("sinceLastInterval", sinceLastInterval);
        pTag.putInt("elapsed", elapsed);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.flashing = pTag.getBoolean("flashing");
        this.powered = pTag.getBoolean("powered");
        this.sinceLastInterval = pTag.getInt("sinceLastInterval");
        this.elapsed = pTag.getInt("elapsed");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        sinceLastInterval++;
        if(powered) {
            if(++elapsed >= powerDuration) {
                powered = false;
                level.setBlock(pos, state.setValue(KeyCardReaderBlock.POWERED, false), 3);
                elapsed = 0;
                sinceLastInterval = 0;
            }
        }
        else if(sinceLastInterval >= flashInterval) {
            flashing = !flashing;
            level.setBlock(pos, state.setValue(KeyCardReaderBlock.FLASH, flashing), 3);
            sinceLastInterval = 0;
        }
    }



    public boolean isFlashing() {
        return flashing;
    }

    public boolean isPowered() {
        return powered;
    }

    public void powerOn() {
        if(!powered) { powered = true;}
    }
}
