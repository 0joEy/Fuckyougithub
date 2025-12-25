package net.henrycmoss.bb.block.entity;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.recipe.CrucibleRecipe;
import net.henrycmoss.bb.screen.CrucibleMenu;
import net.henrycmoss.bb.screen.TestMenu;
import net.henrycmoss.bb.util.BbTags;
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
import net.minecraft.world.item.crafting.Ingredient;
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
import org.lwjgl.panama.Test;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

public class TestBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> true;
                case 1 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 150;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;





    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.TEST_BLOCK.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> TestBlockEntity.this.progress;
                    case 1 -> TestBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> TestBlockEntity.this.progress = pValue;
                    case 1 -> TestBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
        return Component.translatable("bb.block.test");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TestMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) return lazyHandler.cast();
        return super.getCapability(cap);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("progress", this.progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@Nonnull CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.data.set(0, pTag.getInt("progress"));
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(hasRecipe() && !level.isClientSide()) {
            increaseProgress();
            setChanged();

            /*LogUtils.getLogger().info("prog: " + progress);
            LogUtils.getLogger().info("scaled: " + Math.round(((float)(progress * 16 / maxProgress))));
            LogUtils.getLogger().info("scaled data: " + Math.round(((float)(this.data.get(0) * 16 / this.data.get(1)))));
            LogUtils.getLogger().info("data prog: " + this.data.get(0));*/

            if(hasFinished()) {
                craft();
                resetProgress();
            }
        }
        else resetProgress();
    }

    private void craft() {
        Optional<TestRecipe> recipe = getCurrentRecipe();

        ItemStack output = recipe.get().getResultItem(getLevel().registryAccess());

        itemHandler.extractItem(INPUT_SLOT, 1, false);

        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
            itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private void resetProgress() { this.progress = 0; }

    private boolean hasFinished() { return this.progress >= this.maxProgress; }

    private void increaseProgress() {
        this.progress++;
    }
    private boolean hasRecipe() {
        Optional<TestRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) return false;

        ItemStack res = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertIntoOutput(res.getCount()) && canInsertIntoOutput(res.getItem());
    }

    private Optional<TestRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<TestRecipe> r = this.level.getRecipeManager()
                .getRecipeFor(TestRecipe.Type.INSTANCE, inv, this.level);
        LogUtils.getLogger().info("{}", r.isPresent());

        return r;
    }

    private boolean canInsertIntoOutput(int count) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >= itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
    }

    private boolean canInsertIntoOutput(Item item) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }
}
