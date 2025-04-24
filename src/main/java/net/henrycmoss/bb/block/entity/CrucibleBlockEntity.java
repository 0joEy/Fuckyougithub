package net.henrycmoss.bb.block.entity;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.recipe.CrucibleRecipe;
import net.henrycmoss.bb.screen.CrucibleMenu;
import net.henrycmoss.bb.util.BbTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

public class CrucibleBlockEntity extends BlockEntity implements MenuProvider {



    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true;
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 150;

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int OUTPUT_SLOT = 2;





    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.CRUCIBLE_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CrucibleBlockEntity.this.progress;
                    case 1 -> CrucibleBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CrucibleBlockEntity.this.progress = pValue;
                    case 1 -> CrucibleBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
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
        return Component.translatable("bb.block.crucible");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrucibleMenu(pContainerId, pPlayerInventory, this, this.data);
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
        ItemStack res = getCurrentRecipe().get().getResultItem(getLevel().registryAccess());

        itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemHandler.extractItem(INPUT_SLOT_2, 1, false);

        if(res.is(BbTags.Items.forgeTag("gasses"))) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.AMETHYST_BLOCK, 3));
        }
        else {
            itemHandler.setStackInSlot(OUTPUT_SLOT, res);
        }
    }

    private void resetProgress() { this.progress = 0; }

    private boolean hasFinished() { return this.progress >= this.maxProgress; }

    private void increaseProgress() {
        this.progress++;
    }
    private boolean hasRecipe() {
        Optional<CrucibleRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) return false;

        ItemStack res = recipe.get().getResultItem(null);

        ItemStack[] results;

        return canInsertIntoOutput(res.getCount()) && canInsertIntoOutput(res.getItem());
    }

    private Optional<CrucibleRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CrucibleRecipe.Type.INSTANCE, inv, this.level);
    }

    private boolean canInsertIntoOutput(int count) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >= itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
    }

    private boolean canInsertIntoOutput(Item item) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private ItemStack fromIngredient(Ingredient ing) {
        if(Arrays.stream(ing.getItems()).toList().isEmpty()) return ItemStack.EMPTY;

        ItemStack item = Arrays.stream(ing.getItems()).findFirst().get();

        return item;
    }
}
