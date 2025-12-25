package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComplexRecipe implements Recipe<SimpleContainer> {

    private final RecipeType<?> type;

    private final NonNullList<Ingredient> ingredients;

    private final List<ItemStack> results;

    private final ResourceLocation id;

    protected AbstractComplexRecipe(final RecipeType<?> type, final ResourceLocation id, NonNullList<Ingredient> ingredients, final List<ItemStack> results) {
        this.type = type;
        this.id = id;
        this.results = results;
        this.ingredients = ingredients;
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

    public List<ItemStack> assemble(SimpleContainer container, RegistryAccess registryAccess, List<ItemStack> items) {
        List<ItemStack> copies = new ArrayList<>();

        items.forEach((stack) -> copies.add(copies.size(), stack));

        return copies;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return results.get(0);
    }

    public ItemStack getResultItem(RegistryAccess rAccess, int i) {
        return results.get(i);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public static NonNullList<Ingredient> getIngredientsFromJsonObj(JsonObject json) {
        JsonArray entries = GsonHelper.getAsJsonArray(json, "ingredients");

        NonNullList<Ingredient> ing = NonNullList.withSize(2, Ingredient.EMPTY);

        for(int i = 0; i < entries.size(); i++) {
            ing.set(i, Ingredient.fromJson(entries.get(i)));
        }

        return ing;
    }

    public static List<ItemStack> getItemStacksFromJson(JsonArray json) {
        List<ItemStack> items = new ArrayList<>();
        for(JsonElement e : json) {
            if(e instanceof JsonObject o) {
                items.add(ShapedRecipe.itemStackFromJson(o));
            }
        }
        return items;
    }
}
