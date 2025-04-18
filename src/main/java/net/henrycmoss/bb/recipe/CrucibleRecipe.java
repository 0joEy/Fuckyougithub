package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CrucibleRecipe implements Recipe<SimpleContainer> {

    NonNullList<Ingredient> ingredients;

    private final ItemStack result;

    private final ResourceLocation id;

    private final float exp;
    private final double cookTime;

    public CrucibleRecipe(final ResourceLocation id, final ItemStack result, NonNullList<Ingredient> ingredients, final float exp, final double cookTime) {
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

    public double getCookTime() {
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
        return null;
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

            final RecipeType<CrucibleRecipe> recipeType = BbRecipeTypes.CRUCIBLE.get();

            final double cookTime = GsonHelper.getAsDouble(json, "cookTime");
            final float exp = GsonHelper.getAsFloat(json, "exp");

            NonNullList<Ingredient> ing = getIngredientsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));

            if (ing.isEmpty()) throw new JsonParseException("No ingredients found");
            if (cookTime <= 0) throw new JsonParseException("Cook time must be more than 0");
            if (exp <= 0) throw new JsonParseException("exp must be more than 0");

            ItemStack res = getStack(GsonHelper.getAsJsonObject(json, "result"), json);

            LogUtils.getLogger().info(GsonHelper.getAsJsonObject(json, "result").get("item").toString());

            return new CrucibleRecipe(recipeId, res, ing, exp, cookTime) {
                @Override
                public boolean matches(SimpleContainer container, Level level) {
                    return true;
                }
            };
        }

        @Override
        public @Nullable CrucibleRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> ing = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);

            for(int i = 0; i < ing.size(); ++i) {
                ing.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack res = pBuffer.readItem();
            double cookTime = pBuffer.readDouble();
            float exp = pBuffer.readFloat();

            return new CrucibleRecipe(pRecipeId, res, ing, exp, cookTime) {
                @Override
                public boolean matches(SimpleContainer container, Level level) {
                    return true;
                }
            };
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CrucibleRecipe recipe) {

            pBuffer.writeVarInt(recipe.ingredients.size());
            pBuffer.writeDouble(recipe.getCookTime());
            pBuffer.writeFloat(recipe.getExp());

            for(Ingredient ing : recipe.ingredients) {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItem(recipe.result);
        }

        private static NonNullList<Ingredient> getIngredientsFromJson(JsonArray json) {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for (JsonElement element : json) {
                Ingredient ingredient = Ingredient.fromJson(element);
                ingredients.add(ingredient);
            }

            return ingredients;
        }

        private static ItemStack getStack(JsonObject res, JsonObject json) {
            if(!res.has("item")) return ItemStack.EMPTY;

            JsonElement it = res.get("item");

            ResourceLocation key = new ResourceLocation(it.getAsString());

            String keyCut = key.toString().replace("\\", "");
            keyCut = keyCut.replace("\"", "");

            LogUtils.getLogger().info(key.toString());
            LogUtils.getLogger().info(keyCut);

            if (!ForgeRegistries.ITEMS.containsKey(key)) throw new JsonParseException("Unknown item: " + key);

            Item item = ForgeRegistries.ITEMS.getValue(key);

            if (item == Items.AIR) throw new JsonParseException("Invalid item air");
            else if (item == null) throw new JsonParseException("ITem is null");

            return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
        }

        private static String pathType(String prefix, ResourceLocation key) {
            String itemName;
            if (key.getPath().contains(prefix + "block/")) {
                String toCut = prefix + "block/";
                itemName = key.getPath().substring(prefix.length());
            } else if (key.getPath().contains(prefix + "item/")) {
                String toCut = prefix + "item/";
                itemName = key.getPath().substring(prefix.length());
            } else {
                throw new JsonParseException("Invalid classification of item/block: " + key);
            }

            return itemName;
        }
    }
}
