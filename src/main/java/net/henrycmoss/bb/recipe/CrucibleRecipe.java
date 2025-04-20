package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.util.BbTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients;

    private final ItemStack result;

    private final ResourceLocation id;

    private final float exp;
    private final int cookTime;

    public CrucibleRecipe(final ResourceLocation id, final ItemStack result, NonNullList<Ingredient> ingredients, final float exp, final int cookTime) {
        this.id = id;
        this.result = result;
        this.exp = exp;
        this.cookTime = cookTime;
        this.ingredients = ingredients;
    }


    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()) return false;
        return this.ingredients.get(0).test(container.getItem(0)) && this.ingredients.get(1).test(container.getItem(1));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return this.result;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public float getExp() {
        return this.exp;
    }

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
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CrucibleRecipe> {
        private Type() {}

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation("crucible");

    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        private static final ResourceLocation NAME = new ResourceLocation(Bb.MODID, "crucible");

        public @NotNull CrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            JsonArray ings = GsonHelper.getAsJsonArray(json, "ingredients");

            NonNullList<Ingredient> ing = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < ings.size(); i++) {
                ing.set(i, Ingredient.fromJson(ings.get(i)));
            }

            ItemStack res = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            int cookTime = GsonHelper.getAsInt(json, "cookTime");
            float exp = GsonHelper.getAsFloat(json, "exp");

            LogUtils.getLogger().info(GsonHelper.getAsJsonObject(json, "result").get("item").toString());

            return new CrucibleRecipe(recipeId, res, ing, exp, cookTime);
        }

        @Override
        public @Nullable CrucibleRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> ing = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);

            int cookTime = pBuffer.readInt();
            float exp = pBuffer.readFloat();

            for(int i = 0; i < ing.size(); ++i) {
                ing.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack res = pBuffer.readItem();

            return new CrucibleRecipe(pRecipeId, res, ing, exp, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CrucibleRecipe recipe) {

            pBuffer.writeVarInt(recipe.ingredients.size());

            pBuffer.writeInt(recipe.getCookTime());
            pBuffer.writeFloat(recipe.getExp());

            for(Ingredient ing : recipe.ingredients) {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
