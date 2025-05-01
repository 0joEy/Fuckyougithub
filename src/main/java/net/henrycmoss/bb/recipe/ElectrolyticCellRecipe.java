package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.henrycmoss.bb.Bb;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class ElectrolyticCellRecipe extends AbstractComplexRecipe {

    private final int duration;
    private final int initial;

    private final float exp;

    public ElectrolyticCellRecipe(final RecipeType<?> type, final ResourceLocation id, final NonNullList<Ingredient> ingredients, final List<ItemStack> results,
                                  int duration, int initial, float exp) {
        super(BbRecipeTypes.ELECTROLYTIC_CELL.get(), id, ingredients, results);
        this.duration = duration;
        this.initial = initial;
        this.exp = exp;
    }

    public static class Type implements RecipeType<ElectrolyticCellRecipe> {
        private Type() {};

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "electrolytic_cell");
    }

    public static class Serializer implements RecipeSerializer<ElectrolyticCellRecipe> {

        public static Serializer INSTANCE = new Serializer();
        private static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "electrolytic_cell");

        @Override
        public ElectrolyticCellRecipe fromJson(ResourceLocation id, JsonObject json) {

            JsonArray arr = GsonHelper.getAsJsonArray(json, "ingredients");

            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < arr.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(arr.get(i)));
            }

            JsonArray outputs = GsonHelper.getAsJsonArray(json, "results");

            List<ItemStack> results = List.of(Ingredient.fromJson(outputs.get(0)).getItems()[0], Ingredient.fromJson(outputs.get(1)).getItems()[0]);

            int duration = GsonHelper.getAsInt(json, "duration");
            int initial = GsonHelper.getAsInt(json, "initial");

            float exp = GsonHelper.getAsFloat(json, "exp");

            return new ElectrolyticCellRecipe(BbRecipeTypes.ELECTROLYTIC_CELL.get(), id, ingredients, results, duration, initial, exp);
        }

        @Override
        public @Nullable ElectrolyticCellRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            NonNullList<Ingredient> ingredients = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);

            for(int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack i1 = pBuffer.readItem();
            ItemStack i2 = pBuffer.readItem();

            List<ItemStack> results = List.of(i1, i2);

            int duration = pBuffer.readInt();
            int initial = pBuffer.readInt();

            float exp = pBuffer.readFloat();

            return new ElectrolyticCellRecipe(BbRecipeTypes.ELECTROLYTIC_CELL.get(), pRecipeId, ingredients, results, duration, initial, exp);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ElectrolyticCellRecipe recipe) {
            pBuffer.writeVarInt(recipe.getIngredients().size());

            recipe.getIngredients().forEach(ing -> ing.toNetwork(pBuffer));

            pBuffer.writeCollection(recipe.getResults(), FriendlyByteBuf::writeItem);

            pBuffer.writeInt(recipe.getDuration());
            pBuffer.writeInt(recipe.getInitial());

            pBuffer.writeFloat(recipe.getExp());
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return BbRecipeTypes.ELECTROLYTIC_CELL.get();
    }

    public int getDuration() {
        return duration;
    }

    public int getInitial() {
        return initial;
    }

    public float getExp() {
        return exp;
    }
}
