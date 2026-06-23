package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.block.custom.JarBlock;
import net.henrycmoss.bb.recipe.ItemState;
import net.henrycmoss.bb.recipe.JarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Stack;

public class JarBlockEntity extends BlockEntity implements Container {

    private NonNullList<ItemStack> contents = NonNullList.withSize(2, ItemStack.EMPTY);
    private boolean empty;
    private int itemTicks;

    private int time;

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.JAR_BLOCK.get(), pos, state);
        itemTicks = 0;
        empty = true;
    }

    public JarBlockEntity(ItemStack contents, BlockPos pos, BlockState state) {
        this(pos, state);
    }

    public void drops(int slot, double x, double y, double z) {
        Containers.dropItemStack(level, x, y, z, contents.get(slot));
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide()) {
            if (!empty) itemTicks++;
            if (hasRecipe()) {
                //noinspection OptionalGetWithoutIsPresent
                time = getCurrentRecipe().get().getTime();
                if (itemTicks >= time) {
                    craft();
                }
            }
        }
    }


    private Optional<JarRecipe> getCurrentRecipe() {
        SimpleContainer container = new SimpleContainer(2);
        for(ItemStack i : getContents()) {
            container.addItem(i);
        }
        return level.getRecipeManager().getRecipeFor(JarRecipe.Type.INSTANCE, container, level);
    }

    private boolean hasRecipe() {
        return getCurrentRecipe().isPresent();
    }

    private void craft() {
        JarRecipe recipe = getCurrentRecipe().get();
        ItemStack output = recipe.getResultItem(null);
        ItemStack i1 = recipe.getIngredient().get(0).getItems()[0];
        ItemStack i2 = recipe.getIngredient().get(1).getItems()[0];
        ItemStack primaryIng = (linkToStack(i1).getCount() / i1.getCount()) >=
                (linkToStack(i2).getCount() / i2.getCount()) ? i1 : i2;
        ((JarBlock) this.getBlockState().getBlock()).setContents(0, new ItemStack(output.getItem(),
                (linkToStack(primaryIng).getCount() / primaryIng.getCount()) * output.getCount()),
                this.level, this);
    }

    private ItemStack linkToStack(ItemStack ing) {
        for(ItemStack i : contents) {
            if(ing.getItem() == i.getItem()) return i;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.contents = NonNullList.withSize(2, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, contents);
        this.itemTicks = pTag.getInt("itemTicks");
        this.empty = pTag.getBoolean("empty");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", ContainerHelper.saveAllItems(pTag, contents));
        pTag.putInt("itemTicks", itemTicks);
        pTag.putBoolean("empty", empty);
        pTag.putInt("type", getContentsType());
    }

    public NonNullList<ItemStack> getContents() {
        return contents;
    }

    public void setContents(int slot, ItemStack updated) {
        this.contents.set(slot, updated);
        reset();
    }

    public int getContentsType() {
        int type = 0;
        for(ItemStack i : contents) {
            if(ItemState.get(i.getItem()) == ItemState.SOLID) type++;
            else if(ItemState.get(i.getItem()) == ItemState.LIQUID) type += 2;
        }
        return type;
    }

    public ItemState getContentType(int slot) {
        return ItemState.get(contents.get(slot).getItem());
    }

    public boolean isFull() {
        for(ItemStack i : contents) {
            if(i.getMaxStackSize() == i.getCount()
             && i.getItem() != Items.AIR) return true;
        }
        return false;
    }

    public void reset() { itemTicks = 0; }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public boolean stackable() {
        return !isEmpty() && !isFull();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return contents.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack updated = new ItemStack(getContents().get(pSlot).getItem(), getContents().get(pSlot)
                .getCount() - pAmount);
        ((JarBlock) this.getBlockState().getBlock()).setContents(pSlot, updated, this.level, this);
        return updated;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        setContents(pSlot, pStack);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        contents.clear();
        reset();
    }
}
