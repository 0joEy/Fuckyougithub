package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.custom.TestBlock;
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

public class TestRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int craftTime;

    public TestRecipe(NonNullList<Ingredient> ingredients, ItemStack output, ResourceLocation id,
                      int craftTime) {
        this.ingredients = ingredients;
        this.output = output;
        this.id = id;
        this.craftTime = craftTime;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()) return false;
        return ingredients.get(0).test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public static class Type implements RecipeType<TestRecipe> {

        private Type() {};
        public static final Type INSTANCE = new Type();
        public static final String ID = "test";
    }

public static class Serializer implements RecipeSerializer<TestRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Bb.MODID, "test");
}
}
