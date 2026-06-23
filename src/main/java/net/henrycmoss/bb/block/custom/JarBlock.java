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

            for (int i = 1; i > 0; i--) {
                ItemStack current = be.getContents().get(i);
                if (selected.getItem() == Items.BUCKET && be.getContentType(i) == ItemState.LIQUID) {
                    output = current.getItem() instanceof BucketPickup bp ? bp.pickupBlock(level, pPos, state)
                            : new ItemStack(Items.BUCKET, selected.getCount());
                    inv.setItem(inv.selected, output);
                    setContents(i, new ItemStack(current.getItem(), current.getCount() - 1), level, be);
                    return InteractionResult.CONSUME;
                } else if (selected.getItem() == Items.AIR && be.getContentType(i) == ItemState.SOLID) {
                    be.drops(i, player.getX(), player.getY(), player.getZ());
                    setContents(i, Items.AIR.getDefaultInstance(), level, be);
                    return InteractionResult.CONSUME;
                }

                if (selected.getItem() != current.getItem() && be.stackable()) continue;

                if (!be.isFull()) {
                    if(current.getCount() >= current.getMaxStackSize() && current.getItem() != Items.AIR) continue;
                    if (selected.getItem() instanceof BucketItem bucket && bucket != Items.BUCKET) {
                        output = new ItemStack(ExistingLiquidItem.fluidMap.get(bucket.getFluid()),
                                current.getCount() + 1);
                    } else if (selected.getItem() instanceof CustomBucketItem bucket) {
                        output = new ItemStack(bucket.getFluid(),
                                current.getCount() + 1);
                    } else if (selected.getCount() >= 4) {
                        LogUtils.getLogger().info("Solid recognized");
                        int remove = (selected.getCount() / 4) * 4;
                        output = new ItemStack(selected.getItem(), current.getCount() + remove);
                        inv.removeItem(inv.selected, remove);
                    }
                    if(output == ItemStack.EMPTY) continue;
                    setContents(i, output, level, be);
                }
            }
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

    public void setContents(int slot, ItemStack contents, Level level, JarBlockEntity be) {
        be.setContents(slot, contents);
        level.setBlock(be.getBlockPos(), be.getBlockState().setValue(TYPE,
                be.getContentsType()), 3);
        be.setChanged();
    }
}
