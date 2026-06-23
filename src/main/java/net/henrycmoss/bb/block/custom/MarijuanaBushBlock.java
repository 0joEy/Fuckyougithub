package net.henrycmoss.bb.block.custom;

import com.google.common.collect.ImmutableMap;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MarijuanaBushBlock extends BushBlock implements IForgeBlockState {

    private static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

    public MarijuanaBushBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SAPLING_SHAPE = Block.box(1d, 0d, 1d, 5d, 7d, 5d);
    private static final VoxelShape MID_GROWTH_SHAPE = Block.box(1d, 0d, 1d, 10d, 12d,
            10d);
    private static final VoxelShape MATURE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 12.0D, 18.0D, 12.0D);

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        int age = pContext.getLevel().getBlockState(pContext.getClickedPos())
                .is(Blocks.FARMLAND) ? 0 : 2;
        return this.defaultBlockState().setValue(AGE, age);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public ItemStack getCloneItemStack(HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(BbItems.MARIJUANA.get());
    }
}
