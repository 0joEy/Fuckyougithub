package net.henrycmoss.bb.block.custom;

import com.google.common.collect.ImmutableMap;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.jetbrains.annotations.Nullable;

public class MarijuanaBushBlock extends BushBlock implements IForgeBlockState {

    public MarijuanaBushBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape MATURE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 12.0D, 18.0D, 12.0D);


    @Override
    public @Nullable VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return MATURE_SHAPE;
    }

    @Override
    public ItemStack getCloneItemStack(HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(BbItems.MARIJUANA.get());
    }
}
