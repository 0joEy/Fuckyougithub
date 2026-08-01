package net.henrycmoss.bb.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.entity.BbBlockEntities;
import net.henrycmoss.bb.block.entity.KeyCardReaderBlockEntity;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.item.custom.Authorizations;
import net.henrycmoss.bb.item.custom.KeyCardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public class KeyCardReaderBlock extends BaseEntityBlock {
    public static final IntegerProperty AUTHORIZATION = IntegerProperty.create("authorization", 0, 4);
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty FLASH = BooleanProperty.create("flash");

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final int pressedDuration = 15;
    private final int flashInterval = 20;
    private int elapsed;

    protected static final VoxelShape CEILING_X = Block.box(6.0D, 14.0D, 5.0D, 10.0D, 16.0D, 11.0D);
    protected static final VoxelShape CEILING_Z = Block.box(5.0D, 14.0D, 6.0D, 11.0D, 16.0D, 10.0D);
    protected static final VoxelShape FLOOR_X = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 2.0D, 11.0D);
    protected static final VoxelShape FLOOR_Z = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 2.0D, 10.0D);


    protected static final VoxelShape NORTH = Block.box(5.0D, 6.0D, 14.0D, 11.0D, 10.0D, 16.0D);
    protected static final VoxelShape SOUTH = Block.box(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 2.0D);
    protected static final VoxelShape WEST = Block.box(14.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
    protected static final VoxelShape EAST = Block.box(0.0D, 6.0D, 5.0D, 2.0D, 10.0D, 11.0D);

    public KeyCardReaderBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false).setValue(AUTHORIZATION, 0).setValue(FLASH, false));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.setValue(FACING, pMirror.mirror(pState.getValue(FACING)));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        return switch(direction) {
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(AUTHORIZATION, 0).setValue(POWERED, false)
                .setValue(FLASH, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, AUTHORIZATION, POWERED, FLASH);
    }

    public void run(BlockState state, Level level, BlockPos pos) {
        if(!state.getValue(POWERED)) activate(state, level, pos);
    }



    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(++elapsed >= flashInterval) {
            pLevel.setBlock(pPos, pState.setValue(FLASH,
                    !pState.getValue(FLASH)), 3);
            updateNeighbors(pState, pLevel, pPos);
            LogUtils.getLogger().info("flash: " + pLevel.getBlockState(pPos).getValue(FLASH));
            elapsed = 0;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if(!level.isClientSide()) {
            if(player.getItemInHand(pHand).getItem() instanceof KeyCardItem card
                    && state.getValue(KeyCardReaderBlock.AUTHORIZATION) + 1 <= card.get().getLevel()) {
                ((KeyCardReaderBlock) state.getBlock()).run(state, level, pos);
                return InteractionResult.SUCCESS;
            }
            else if(player.getItemInHand(pHand).is(BbItems.ADMIN_REMOTE.get())) {
                ((KeyCardReaderBlock) state.getBlock()).cycleLevel(state, level, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }



    public void activate(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(POWERED, true), 3);
        level.scheduleTick(pos, this, pressedDuration);
        if(level.getBlockEntity(pos) instanceof KeyCardReaderBlockEntity entity) {
            entity.powerOn();
        }
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return pState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return pState.getValue(POWERED) && pState.getValue(FACING) == pDirection ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    private void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    public void cycleLevel(BlockState state, Level level, BlockPos pos) {
        int auth = state.getValue(AUTHORIZATION);
        if(++auth > 4) auth = 0;
        level.setBlock(pos, state.setValue(AUTHORIZATION,
                auth), 3);
        updateNeighbors(state, level, pos);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if(pState.getValue(FACING) == Direction.DOWN || pState.getValue(FACING) == Direction.UP) {
            pLevel.setBlock(pPos, pOldState, 3);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new KeyCardReaderBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, BbBlockEntities.KEY_CARD_READER_BE.get(),
                (level, pos, state, be) -> be.tick(level, pos, state));
    }
}
