package net.henrycmoss.bb.block.custom;

import net.henrycmoss.bb.block.entity.BbBlockEntities;
import net.henrycmoss.bb.block.entity.TubBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class TubBlock extends BaseEntityBlock {

    public static final VoxelShape shape = Block.box(0, 0, 0, 16, 16, 16);

    public TubBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHit) {
        if(!level.isClientSide()) {
            TubBlockEntity be = (TubBlockEntity) level.getBlockEntity(pos);
            ItemStack selected = player.getItemInHand(hand);
            if(be != null) {
                if (selected.getItem() instanceof BucketItem && be.getFluidTank() != null) {
                    if (selected.is(Items.BUCKET) && !be.getFluidTank().isEmpty()) {
                        ItemStack newBucket = be.getFluidTank().getFluid().getFluid().getBucket().getDefaultInstance();
                        be.getFluidTank().drain(be.getFluidTank().getFluid(), IFluidHandler.FluidAction.EXECUTE);
                        player.setItemInHand(hand, newBucket);
                        level.sendBlockUpdated(pos, state, state, 3);
                        be.setChanged();
                        return InteractionResult.SUCCESS;
                    } else if (be.getFluidTank().isEmpty()) {
                        FluidStack toInsert = new FluidStack(((BucketItem) selected.getItem()).getFluid(), 1000);
                        player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                        be.getFluidTank().setFluid(toInsert);
                        level.sendBlockUpdated(pos, state, state, 3);
                        be.setChanged();
                        return InteractionResult.SUCCESS;
                    }
                } else if (selected.isEmpty() && !be.getItemHandler().getStackInSlot(0).isEmpty()) {
                    be.drops();
                    level.sendBlockUpdated(pos, state, state, 3);
                    be.setChanged();
                    return InteractionResult.SUCCESS;
                }
                else if(be.getItemHandler().getStackInSlot(0).isEmpty()) {
                    be.getItemHandler().setStackInSlot(0, selected);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TubBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, BbBlockEntities.TUB_BE.get(), (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
}
