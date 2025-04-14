package net.henrycmoss.bb.block.custom;

import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockState;

public class ShroomPatchBlock extends BushBlock implements IForgeBlockState {

    public ShroomPatchBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 12.0D, 18.0D, 12.0D);

    @Override
    public ItemStack getCloneItemStack(HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(BbItems.MAGIC_MUSHROOMS.get());
    }

    public static VoxelShape getShape() {
        return SHAPE;
    }
}
