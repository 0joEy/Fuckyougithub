package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class ElectrolyticCellRecipe extends AbstractComplexRecipe {

    private final int duration;
    private final int initial;

    private final float exp;

    public ElectrolyticCellRecipe(final RecipeType<?> type, final ResourceLocation id, final NonNullList<Ingredient> ingredients, final List<ItemStack> results,
                                  int duration, int initial, float exp) {
        super(BbRecipes., id, ingredients, results);
        this.duration = duration;
        this.initial = initial;
        this.exp = exp;
    }

    public static class Type implements RecipeType<ElectrolyticCellRecipe> {
        private Type() {};

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation("electrolytic_cell");
    }

    public static class Serializer implements RecipeSerializer<ElectrolyticCellRecipe> {

        public static Serializer INSTANCE = new Serializer();
        private static final ResourceLocation ID = new ResourceLocation("electrolytic_cell");

        @Override
        public ElectrolyticCellRecipe fromJson(ResourceLocation id, JsonObject json) {

            RecipeType<ElectrolyticCellRecipe> type = BbRecipeTypes.CRUCIBLE.get();

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

            return new ElectrolyticCellRecipe()
        }
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
