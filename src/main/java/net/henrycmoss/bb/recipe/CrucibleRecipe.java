package net.henrycmoss.bb.recipe;

import com.google.common.collect.Lists;
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
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        List<ItemStack> items = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            items.add(container.getItem(i));
        }

        if(items.size() != ingredients.size()) return false;

        List<Ingredient> remaining = new ArrayList<>(ingredients);

        for(ItemStack i : items) {
            boolean matches = false;

            for(Iterator<Ingredient> it = remaining.iterator(); it.hasNext(); ) {
                Ingredient ing = it.next();
                if(ing.test(i)) {
                    matches = true;
                    it.remove();
                    break;
                }
            }

            if(!matches) return false;
        }
        return true;
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
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CrucibleRecipe> {
        private Type() {}

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "crucible");

    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        private static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "crucible");

        public @NotNull CrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            JsonArray jsonIngredients = GsonHelper.getAsJsonArray(json, "ingredients");

            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < ingredients.size(); i++) {
                Ingredient ing = Ingredient.fromJson(jsonIngredients.get(i));
                LogUtils.getLogger().info("{}", Arrays.stream(ing.getItems()).findFirst().get());
                ingredients.set(i, ing);
            }

            ItemStack res = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            int cookTime = GsonHelper.getAsInt(json, "cookTime");
            float exp = GsonHelper.getAsFloat(json, "exp");

            LogUtils.getLogger().info(GsonHelper.getAsJsonObject(json, "result").get("item").toString());

            return new CrucibleRecipe(recipeId, res, ingredients, exp, cookTime);
        }

        @Override
        public @Nullable CrucibleRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> ing = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);

            for(int i = 0; i < ing.size(); ++i) {
                ing.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack result = pBuffer.readItem();

            int cookTime = pBuffer.readInt();
            float exp = pBuffer.readFloat();

            return new CrucibleRecipe(pRecipeId, result, ing, exp, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CrucibleRecipe recipe) {

            pBuffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ing : recipe.ingredients) {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItem(recipe.getResultItem(null));

            pBuffer.writeInt(recipe.getCookTime());
            pBuffer.writeFloat(recipe.getExp());
        }
    }
}
