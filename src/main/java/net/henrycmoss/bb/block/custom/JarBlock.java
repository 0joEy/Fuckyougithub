package net.henrycmoss.bb.block.custom;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.block.entity.BbBlockEntities;
import net.henrycmoss.bb.block.entity.JarBlockEntity;
import net.henrycmoss.bb.item.custom.CustomBucketItem;
import net.henrycmoss.bb.item.custom.ExistingLiquidItem;
import net.henrycmoss.bb.item.custom.LiquidItem;
import net.henrycmoss.bb.recipe.ItemState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends BaseEntityBlock {

    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);

    public JarBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, ItemState.NONE.getId()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(TYPE, ItemState.NONE.getId());
    }

    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pPos, Player player, InteractionHand pHand,
                                 BlockHitResult pHit) {
        JarBlockEntity be = (JarBlockEntity) level.getBlockEntity(pPos);

        if(be != null) {
            Inventory inv = player.getInventory();
            ItemStack selected = inv.getSelected();
            ItemStack output = ItemStack.EMPTY;
            int unit = 2;

            if(be.isEmpty()) {
                if(selected.isEmpty()) return InteractionResult.CONSUME;
                else if(selected.getItem() instanceof BucketItem bucket
                    && bucket != Items.BUCKET) {
                    output = new ItemStack(ExistingLiquidItem.fluidMap.get(bucket.getFluid()));
                }
                else if(selected.getCount() >= unit) {
                    int amount = ((int) selected.getCount() / unit) * unit;
                    output = new ItemStack(selected.getItem(), amount);
                    if(selected.getCount() - amount <= 0) player.setItemInHand(pHand, ItemStack.EMPTY);
                    else player.setItemInHand(pHand, new ItemStack(selected.getItem(),
                            selected.getCount() - amount));
                }
            }
            else {
                if(selected.is(Items.BUCKET) && be.getItem().getItem() instanceof ExistingLiquidItem fluid) {
                    be.clearContent();
                    player.setItemInHand(pHand, fluid.getFluid().getBucket().getDefaultInstance());
                }
                else {
                    be.drops(pPos.getX(), pPos.getY(), pPos.getZ());
                }
            }
            setContents(output, level, be);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new JarBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
                                                                            BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, BbBlockEntities.JAR_BLOCK.get(),
                (level, pos, state, jarBlockEntity)
                        -> jarBlockEntity.tick(level, pos, state));
    }

    public void setContents(ItemStack contents, Level level, JarBlockEntity be) {
        be.setItem(contents);
        int state = contents == ItemStack.EMPTY ? 0 : 1;
        BlockPos pos = be.getBlockPos();
        level.setBlock(pos, level.getBlockState(pos).setValue(TYPE, state), 3);
    }
}
