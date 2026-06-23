package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.custom.TestBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public static class Type implements RecipeType<TestRecipe> {

        private Type() {};
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "test");
    }

    public static class Serializer implements RecipeSerializer<TestRecipe> {
            public static final Serializer INSTANCE = new Serializer();
            public static final ResourceLocation ID =
                    new ResourceLocation(Bb.MODID, "test");

        @Override
        public TestRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            NonNullList<Ingredient> ingredients = NonNullList.withSize(1, Ingredient.EMPTY);
            JsonArray jsonIngredients = GsonHelper.getAsJsonArray(json, "ingredients");

            for(int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(jsonIngredients.get(i)));
            }

            int craftTime = json.get("craftTime").getAsInt();

            return new TestRecipe(ingredients, result, id, craftTime);
        }

        @Override
        public @Nullable TestRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack result = buf.readItem();
            int craftTime = buf.readInt();

            return new TestRecipe(ingredients, result, id, craftTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TestRecipe recipe) {
                buf.writeInt(recipe.getIngredients().size());

                for(Ingredient i : recipe.getIngredients()) {
                    i.toNetwork(buf);
                }

                buf.writeItemStack(recipe.getResultItem(null), false);
                buf.writeInt(recipe.getCraftTime());
        }
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}
