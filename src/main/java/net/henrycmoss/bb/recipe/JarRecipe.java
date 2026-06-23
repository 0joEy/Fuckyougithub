package net.henrycmoss.bb.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class JarRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int time;

    public JarRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int time) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.time = time;

        LogUtils.getLogger().info("Loaded recipe id: {}", id.toString());
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        List<ItemStack> list = List.of(pContainer.getItem(0), pContainer.getItem(1));
        for(Ingredient i : ingredients) {
            boolean matches = false;
            ItemStack ing = i.getItems()[0];
            for(Iterator<ItemStack> it = list.iterator(); it.hasNext();) {
                ItemStack item = it.next();
                if(ing.getItem() == item.getItem() && ing.getCount() >= item.getCount()) {
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
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Type implements RecipeType<JarRecipe> {

        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "jar");

    }

    public static class Serializer implements RecipeSerializer<JarRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "jar");

        @Override
        public JarRecipe fromJson(ResourceLocation id, JsonObject json) {

            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
            JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
            for(int i = 0; i < array.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(array.get(i)));
            }
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"),
                    false);
            int time = GsonHelper.getAsInt(json, "time");

            return new JarRecipe(id, ingredients, result, time);
        }

        @Override
        public @Nullable JarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
            for(int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack result = buf.readItem();
            int time = buf.readInt();

            return new JarRecipe(id, ingredients, result, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, JarRecipe recipe) {
            for(Ingredient i : recipe.getIngredient()) i.toNetwork(buf);
            buf.writeItem(recipe.getResultItem(null));
            buf.writeInt(recipe.getTime());
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public NonNullList<Ingredient> getIngredient() {
        return ingredients;
    }

    public int getTime() {
        return time;
    }
}
