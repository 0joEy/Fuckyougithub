package net.henrycmoss.bb.block.entity;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.recipe.TubRecipe;
import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TubBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    private final FluidTank fluidTank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    private int time = 0;
    private boolean cooking = false;
    private boolean wasCooking = cooking;

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public TubBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.TUB_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(!level.isClientSide()) {
            cooking = level.getBlockState(pos.below()).is(Blocks.CAMPFIRE);
            if(wasCooking != cooking) {
                setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                this.saveAdditional(new CompoundTag());
            }
            Vec3 center = Vec3.atCenterOf(pos);
            Vec3 particleOrigin = this.fluidTank.isEmpty() ? center.relative(Direction.DOWN, 0.2f) :
                    center.relative(Direction.UP, 0.5f);
            if(cooking) {
                if(!this.fluidTank.isEmpty() && level.getRandom().nextFloat() > 0.4f) {
                    Vec3 particlePos = particleOrigin.add(level.getRandom().nextFloat() - 0.5, 0,
                            level.getRandom().nextFloat() - 0.5);
                    float pX = (level.getRandom().nextFloat() * 360) - 180;
                    float pY = (level.getRandom().nextFloat() * 120) - 60;
                    Vec3 particleMovement = ShootingTools.shootFromRotation(pX, pY, 0, 3f);
                    ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, particlePos.x, particlePos.y, particlePos.z,
                            0, particleMovement.x, particleMovement.y, particleMovement.z, 1);
                }
                if(!this.getRenderedItem().isEmpty() && level.getRandom().nextFloat() > 0.4f) {
                    Vec3 particlePos = particleOrigin.add((level.getRandom().nextFloat() - 0.5) * 0.05f, 0,
                            (level.getRandom().nextFloat() - 0.5) * 0.05f);
                    ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0, 0, 1);
                }
            }
            if(hasRecipe()) {
                TubRecipe recipe = getCurrentRecipe().get();
                HolderSet<Block> set = HolderSet.direct(Holder.direct(Blocks.FIRE), Holder.direct(Blocks.CAMPFIRE),
                        Holder.direct(Blocks.SOUL_CAMPFIRE), Holder.direct(Blocks.SOUL_FIRE));
                if(!recipe.isCooked() || cooking) {
                    step();
                    if (this.time >= recipe.getTime()) {
                        if (recipe.isFluidRecipe()) craftFluid(recipe.getResultFluid());
                        else craftSolid(recipe.getResultItem());
                        time = 0;
                    }
                }
            }
            wasCooking = cooking;
        }
    }

    private boolean hasRecipe() {
        Optional<TubRecipe> recipe = getCurrentRecipe();
        return recipe.isPresent();
    }

    public Optional<TubRecipe> getCurrentRecipe() {
        TubContainer inv = new TubContainer(itemHandler.getSlots(), fluidTank.getFluid());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        return this.level.getRecipeManager()
                .getRecipeFor(TubRecipe.Type.INSTANCE, inv, this.level);
    }

    public void craftFluid(FluidStack result) {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        fluidTank.setFluid(result);
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void craftSolid(ItemStack result) {
        fluidTank.drain(fluidTank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        itemHandler.setStackInSlot(0, result);
        this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            // Optional: Only allow item insertion from the TOP side
            if (side == Direction.UP || side == null) {
                return lazyItemHandler.cast();
            }
            return LazyOptional.empty(); // Deny item access from sides/bottom
        }

        // B. Handle Fluid Handler Automation (e.g., Fluid Pipes)
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            // Let fluid pipes pump from any side
            return lazyFluidHandler.cast();
        }

        // C. Fallback to vanilla capability handling
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> fluidTank);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        fluidTank.writeToNBT(pTag);
        pTag.putInt("time", time);
        pTag.putBoolean("cooking", this.cooking);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("inventory")) {
            CompoundTag invTag = pTag.getCompound("inventory");

            // Let Forge deserialize the packet first
            this.itemHandler.deserializeNBT(invTag);

            // Explicitly double-check if the slot was meant to be empty!
            // If the NBT doesn't specifically declare an item in Slot 0,
            // we MUST force the client to clear Slot 0.
            if (!invTag.contains("Items") || invTag.getList("Items", 10).isEmpty()) {
                this.itemHandler.setStackInSlot(0, net.minecraft.world.item.ItemStack.EMPTY);
            }
        }
        else {
            this.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        }
        fluidTank.readFromNBT(pTag);
        time = pTag.getInt("time");
        this.cooking = pTag.getBoolean("cooking");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if(tag != null) {
            this.load(tag);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this, be -> ((TubBlockEntity) be).getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.load(tag);
            if (this.level != null && this.level.isClientSide()) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void step() { time++; }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getRenderedItem() {
        return itemHandler.getStackInSlot(0);
    }
}
