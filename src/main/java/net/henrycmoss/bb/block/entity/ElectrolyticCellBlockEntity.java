package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.recipe.ElectrolysisRecipe;
import net.henrycmoss.bb.screen.ElectrolyticCellMenu;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ElectrolyticCellBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true;
                case 2, 3, 4, 5, 6 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ContainerData data;
    private int progress = 0;
    private int max = 150;

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int ENERGY_SLOT = 2;
    private static int OUTPUT_SLOT_1 = 3;
    private static int OUTPUT_SLOT_2 = 4;
    private static int EXCESS_SLOT_1;
    private static int EXCESS_SLOT_2;

    boolean temp = true;

    ItemStack[] results;
    ItemStack[] ingredients;

    private int initProduct = 20;

    public ElectrolyticCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BbBlockEntities.ELECTROLYTIC_CELL.get(), pPos, pBlockState);
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
        return Component.literal("Electrolytic Cell");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ElectrolyticCellMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("progress", this.progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag);
        this.data.set(0, pTag.getInt("progress"));
        super.load(pTag);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(!level.isClientSide()) {
            if (hasRecipe()) {
                increaseProgress();
                setChanged();

                if (hasFinished()) {
                    craft();
                    resetProgress();
                }
            } else resetProgress();
        }
    }

    private void craft() {
        //ItemStack res = getCurrentRecipe().get().getResultItem(getLevel().registryAccess());

        itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemHandler.extractItem(INPUT_SLOT_2, 1, false);

        itemHandler.setStackInSlot(OUTPUT_SLOT_1, results[0]);
        itemHandler.setStackInSlot(OUTPUT_SLOT_2, results[1]);
    }

    private void resetProgress() { this.progress = 0; }

    private boolean hasFinished() { return this.progress >= this.max; }

    private void increaseProgress() {
        this.progress++;
    }
    private boolean hasRecipe() {
        Optional<ElectrolysisRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) return false;

        int[] slots = new int[2];

        for(int i = 0; i < slots.length; i++) {

        }

        ItemStack[] results = getCurrentRecipe().get().getResults().toArray(new ItemStack[0]);

        boolean[] conditions = new boolean[2];

        for(int i = 0; i < results.length; i++) {
            conditions[i] = canInsertIntoOutput(results[i].getCount(), slots[i]) && canInsertIntoOutput(results[i].getItem(), slots[i]);
        }

        if (conditions[0] && conditions[1]) {
            this.results = results;
            this.ingredients = getCurrentRecipe().get().getResults().toArray(new ItemStack[0]);
            return true;
        }
        return false;
    }

    private boolean canInsertIntoOutput(int count) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT_1).getMaxStackSize() >= itemHandler.getStackInSlot(OUTPUT_SLOT_1).getCount() + count;
    }

    private boolean canInsertIntoOutput(Item item) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT_1).isEmpty() || itemHandler.getStackInSlot(OUTPUT_SLOT_1).is(item);
    }
    /*
    public void tick(Level level, BlockState state, BlockPos pos) {
        boolean outputsEmptyOrReceivable = isOutputSlotEmptyOrReceivable(OUTPUT_SLOT_1) && isOutputSlotEmptyOrReceivable(OUTPUT_SLOT_2);
        if(hasRecipe() && !level.isClientSide() && outputsEmptyOrReceivable) {
            increaseProgress();
            setChanged();

            if(hasFinished()) {
                craft();
                resetProgress();
            }
        }
        else resetProgress();
    }

    private void craft() {
        this.itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT_2, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT_1_1, results[0]);
        this.itemHandler.setStackInSlot(OUTPUT_SLOT_1_2, results[1]);
    }

    private void resetProgress() { this.progress = 0; }

    private boolean hasFinished() { return this.progress >= this.max; }

    private void increaseProgress() {
        this.progress++;
    }
    /*private boolean hasRecipe() {

        if(hasIngredients()) {

            int[] outputs = {OUTPUT_SLOT_1, OUTPUT_SLOT_2};

            boolean[] conditions = {false, false};

            for (int i = 0; i < conditions.length; i++) {
                int slot = outputs[i];
                conditions[i] = canInsertIntoOutput(results[i].getCount(), slot) && canInsertIntoOutput(results[i].getItem(), slot);
            }

            return conditions[0] && conditions[1];
        }
        return false;
    }*/

    private boolean hasIngredients() {
        boolean f1 = itemHandler.getStackInSlot(INPUT_SLOT_1).is(ingredients[INPUT_SLOT_1].getItem());
        boolean f2 = itemHandler.getStackInSlot(INPUT_SLOT_2).is(ingredients[INPUT_SLOT_2].getItem());

        return f1 && f2;
    }

    private Optional<ElectrolysisRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ElectrolysisRecipe.Type.INSTANCE, inv, level);
    }

    private boolean canInsertIntoOutput(int count, int slot) {
        return itemHandler.getStackInSlot(slot).isEmpty() || itemHandler.getStackInSlot(slot).getCount() + count < itemHandler.getSlotLimit(slot);
    }

    private boolean canInsertIntoOutput(Item item, int slot) {
        return itemHandler.getStackInSlot(slot).isEmpty() || itemHandler.getStackInSlot(slot).is(item);
    }

    public int getInitProduct() { return initProduct; }

    public void setInitProduct(int prod) { initProduct = prod; }

    private boolean isOutputSlotEmptyOrReceivable(int slot) {
        return this.itemHandler.getStackInSlot(slot).isEmpty() ||
                this.itemHandler.getStackInSlot(slot).getCount() < this.itemHandler.getStackInSlot(slot).getMaxStackSize();
    }
}