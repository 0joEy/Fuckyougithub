package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.block.custom.JarBlock;
import net.henrycmoss.bb.recipe.ItemState;
import net.henrycmoss.bb.recipe.JarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Optional;
import java.util.Stack;

public class JarBlockEntity extends BlockEntity implements Container {

    private ItemStack input = ItemStack.EMPTY;
    private int itemTicks;

    private int time;

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(BbBlockEntities.JAR_BLOCK.get(), pos, state);
        itemTicks = 0;
    }

    public JarBlockEntity(ItemStack contents, BlockPos pos, BlockState state) {
        this(pos, state);
    }

    public void drops(double x, double y, double z) {
        Containers.dropItemStack(level, x, y, z, input);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide()) {
            if (!isEmpty()) itemTicks++;
            if (hasRecipe()) {
                //noinspection OptionalGetWithoutIsPresent
                time = getCurrentRecipe().get().getTime();
                if (itemTicks >= time) {
                    craft();
                    itemTicks = 0;
                }
            }
        }
    }


    private Optional<JarRecipe> getCurrentRecipe() {
        SimpleContainer container = new SimpleContainer(1);
        container.addItem(input);
        return level.getRecipeManager().getRecipeFor(JarRecipe.Type.INSTANCE, container, level);
    }

    private boolean hasRecipe() {
        return getCurrentRecipe().isPresent();
    }

    private void craft() {
        JarRecipe recipe = getCurrentRecipe().get();
        ItemStack output = recipe.getResultItem(null);
        ItemStack ingredient = recipe.getIngredient().get(0).getItems()[0];
        ((JarBlock) this.getBlockState().getBlock()).setContents(new ItemStack(output.getItem(),
                (input.getCount() / ingredient.getCount())
                        * output.getCount()), this.level, this);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.input = ItemStack.of(pTag.getCompound("input"));
        this.itemTicks = pTag.getInt("itemTicks");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("input", input.serializeNBT());
        pTag.putInt("itemTicks", itemTicks);
        pTag.putBoolean("empty", isEmpty());
        //pTag.putInt("type", getContentsType());
    }

    public ItemStack getItem() {
        return input;
    }

    public void setItem(ItemStack stack) {
        input = stack;
    }




    public void reset() { itemTicks = 0; }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return input;
    }

    @Override
    public ItemStack removeItem(int slot, int pAmount) {
        ItemStack updated = new ItemStack(input.getItem(), input.getCount() - pAmount);
        ((JarBlock) this.getBlockState().getBlock()).setContents(updated, this.level, this);
        return updated;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return null;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        setItem(pStack);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        input = ItemStack.EMPTY;
        reset();
    }
}
