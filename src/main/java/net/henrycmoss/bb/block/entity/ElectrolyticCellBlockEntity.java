package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.recipe.CrucibleRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ElectrolyticCellBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler() {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> itemHandler.getStackInSlot(INPUT_SLOT_1).is(BbItems.SALT.get());
                case 1 -> itemHandler.getStackInSlot(INPUT_SLOT_2).is(Items.WATER_BUCKET);
                case 2 -> itemHandler.getStackInSlot(ENERGY_SLOT).is(BbItems.BATTERY.get());
                case 3, 4, 5, 6 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ContainerData data;
    private int progress;
    private int max;

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int ENERGY_SLOT = 2;
    private static int OUTPUT_SLOT_1;
    private static int OUTPUT_SLOT_2;
    private static int EXCESS_SLOT_1;
    private static int EXCESS_SLOT_2;

    public ElectrolyticCellBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ElectrolyticCellBlockEntity.this.progress;
                    case 1 -> ElectrolyticCellBlockEntity.this.max;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case 0 -> ElectrolyticCellBlockEntity.this.progress = val;
                    case 1 -> ElectrolyticCellBlockEntity.this.max = val;
                }
            }

            @Override
            public int getCount() {
                return itemHandler.getSlots();
            }
        };
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("bb.block.electrolytic_cell");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    @Override
    public void onLoad() {
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("progress", this.progress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag);
        this.data.set(0, pTag.getInt("progress"));
    }

    private void resetProgress() { this.progress = 0; }

    private boolean hasFinished() { return this.progress >= this.max; }

    private void increaseProgress() {
        this.progress++;
    }
    private boolean hasRecipe() {
        Optional<CrucibleRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) return false;

        ItemStack res = recipe.get().getResultItem(null);
        int slot = 3;

        return canInsertIntoOutput(res.getCount(), slot) && canInsertIntoOutput(res.getItem(), slot);
    }

    private Optional<CrucibleRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CrucibleRecipe.Type.INSTANCE, inv, this.level);
    }

    private boolean canInsertIntoOutput(int count, int slot) {
        return itemHandler.getStackInSlot(slot).getMaxStackSize() >= itemHandler.getStackInSlot(slot).getCount() + count;
    }

    private boolean canInsertIntoOutput(Item item, int slot) {
        return itemHandler.getStackInSlot(slot).isEmpty() || itemHandler.getStackInSlot(slot).is(item);
    }
}
