package net.henrycmoss.bb.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ElectrolyticCellRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients;

    private final NonNullList<ItemStack> results;

    private final ResourceLocation id;

    private final int duration;
    private final int initProduct;

    private final float exp;

    ElectrolyticCellRecipe(final ResourceLocation id, final NonNullList<ItemStack> results, NonNullList<Ingredient> ingredients, final int duration, final int initProduct,
                           final float exp) {
        this.id = id;
        this.results = results;
        this.ingredients = ingredients;
        this.duration = duration;
        this.initProduct = initProduct;
        this.exp = exp;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) return false;
        return this.ingredients.get(0).test(pContainer.getItem(0)) && this.ingredients.get(1).test(pContainer.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return this.results.get(0).copy();
    }

    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess, int i) {
        return i > 0 && i < container.getContainerSize() ? this.results.get(i).copy() : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return results.get(0);
    }

    public ItemStack getResultItem()

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
