package net.henrycmoss.bb.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EtherBlock extends Block {
    public EtherBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 300;
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction,
                             @Nullable LivingEntity igniter) {
        super.onCaughtFire(state, level, pos, direction, igniter);
        level.setBlock(pos.east(1).above(), Blocks.FIRE.defaultBlockState(), 2);
        level.setBlock(pos.west(1).above(), Blocks.FIRE.defaultBlockState(), 2);
        level.setBlock(pos.north(1).above(), Blocks.FIRE.defaultBlockState(), 2);
        level.setBlock(pos.south(1).above(), Blocks.FIRE.defaultBlockState(), 2);
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 10, 0);
    }
}
