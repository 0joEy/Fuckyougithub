package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.henrycmoss.bb.Bb;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectrolysisRecipe extends AbstractComplexRecipe {

    private final int duration;
    private final int initial;

    private final float exp;

    public ElectrolysisRecipe(final RecipeType<?> type, final ResourceLocation id, final NonNullList<Ingredient> ingredients, final List<ItemStack> results,
                              int duration, int initial, float exp) {
        super(BbRecipeTypes.ELECTROLYSIS.get(), id, ingredients, results);
        this.duration = duration;
        this.initial = initial;
        this.exp = exp;
    }

    public static class Type implements RecipeType<ElectrolysisRecipe> {
        private Type() {};

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID,"electrolysis");
    }

    public static class Serializer implements RecipeSerializer<ElectrolysisRecipe> {

        public static Serializer INSTANCE = new Serializer();
        private static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "electrolysis");

        @Override
        public ElectrolysisRecipe fromJson(ResourceLocation id, JsonObject json) {

            NonNullList<Ingredient> ingredients = AbstractComplexRecipe.getIngredientsFromJsonObj(json);

            JsonArray outputs = GsonHelper.getAsJsonArray(json, "results");

            List<ItemStack> results = AbstractComplexRecipe.getItemStacksFromJson(outputs);

            int duration = GsonHelper.getAsInt(json, "duration");
            int initial = GsonHelper.getAsInt(json, "initial");

            float exp = GsonHelper.getAsFloat(json, "exp");

            return new ElectrolysisRecipe(BbRecipeTypes.ELECTROLYSIS.get(), id, ingredients, results, duration, initial, exp);
        }

        @Override
        public @Nullable ElectrolysisRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

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

            return new ElectrolysisRecipe(BbRecipeTypes.ELECTROLYSIS.get(), pRecipeId, ingredients, results, duration, initial, exp);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ElectrolysisRecipe recipe) {
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
        return BbRecipeTypes.ELECTROLYSIS.get();
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
