package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.SlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlidingDoorBlockEntity extends BlockEntity {

    private boolean extending;
    private boolean retracting;
    private DoubleBlockHalf half;
    private float progress0;
    private float progress;
    private int deathTicks;

    private Direction direction;
    private BlockPos doorPos;

    public SlidingDoorBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.SLIDING_DOOR.get(), pos, state);
        half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
    }

    public float getProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.progress0, this.progress);
    }

    public float getExtendedProgress(float progress) {
        return extending ? progress - 1f : 1f - progress;
    }

    public float getXOffset(float partialTicks) {
        return this.direction.getStepX() * this.getExtendedProgress(getProgress(partialTicks));
    }

    public float getYOffset(float partialTicks) {
        return this.direction.getStepY() * getExtendedProgress(getProgress(partialTicks));
    }

    public float getZOffset(float partialTicks) {
        return this.direction.getStepZ() * getExtendedProgress(getProgress(partialTicks));
    }

    public Direction getDirection() {
        return direction;
    }

    public void tick(Level level, BlockPos pos, BlockState state, SlidingDoorBlockEntity be) {
        be.progress = be.progress0;
        if((be.progress >= 1f || be.progress <= 0f) && (be.extending || be.retracting)) {
            if(level.isClientSide() && deathTicks < 5) {
                deathTicks++;
            }
            else {
                if(be.extending) level.setBlock(doorPos, BbBlocks.SLIDING_DOOR.get().defaultBlockState(), 3);
                level.removeBlockEntity(pos);
                this.setRemoved();
                finalTick();
                extending = false;
                retracting = false;
            }
        }
        else {
            be.progress = be.extending || !be.retracting ? be.progress + 0.5f : be.progress - 0.5f;
        }
    }

    public VoxelShape getCollisionShape(Level level, BlockPos pos, BlockState state) {
        if(extending) {
            return state.getCollisionShape(level, pos);
        }
        else return Shapes.empty();
    }

    private void finalTick() {
        this.progress = extending ? 1f : 0f;
        this.progress0 = extending ? 0f : 1f;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.progress = pTag.getFloat("progress");
        this.direction = Direction.from3DDataValue(pTag.getInt("facing"));
        this.extending = pTag.getBoolean("extending");
        this.retracting = pTag.getBoolean("retracting");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("facing", this.direction.get3DDataValue());
        pTag.putFloat("progress", this.progress);
        pTag.putBoolean("extending", this.extending);
        pTag.putBoolean("retracting", this.retracting);
        super.saveAdditional(pTag);
    }

    public void beginOpen(BlockPos pos, BlockState state) {
        extending = true;
        retracting = false;
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Vec3i v = state.getValue(BlockStateProperties.DOOR_HINGE).equals(DoorHingeSide.RIGHT) ? dir.getCounterClockWise().getNormal() : dir.getClockWise().getNormal();
        doorPos = pos.offset(v);
    }

    public void beginClosing(BlockPos pos, BlockState state) {
        extending = false;
        retracting = true;
        doorPos = pos;
    }

    public DoubleBlockHalf getHalf() {
        return half;
    }

    public BlockPos getDoorPos() {
        return doorPos;
    }
}
