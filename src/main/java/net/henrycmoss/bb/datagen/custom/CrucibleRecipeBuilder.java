package net.henrycmoss.bb.datagen.custom;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.recipe.CrucibleRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CrucibleRecipeBuilder implements RecipeBuilder {
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Item result;
    private final int count;
    private final int cookTime;
    private final float exp;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public CrucibleRecipeBuilder(NonNullList<ItemStack> ingredients, Item result, int count, int cookTime, float exp) {
        this.result = result;
        for(ItemStack i : ingredients) this.ingredients.add(Ingredient.of());
        this.count = count;
        this.cookTime = cookTime;
        this.exp = exp;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new ShapelessRecipeBuilder.Result(pRecipeId,
                this.result, this.count, "", CraftingBookCategory.MISC,
                this.ingredients, this.advancement, new ResourceLocation(
                        pRecipeId.getNamespace(), "recipes/" + pRecipeId.getPath())));
    }

    public static class Result implements FinishedRecipe{
        private final ResourceLocation id;
        private final NonNullList<Ingredient> ingredients;
        private final Item result;
        private final int count;
        private final int cookTime;
        private final float exp;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, NonNullList<Ingredient> ingredients, Item result, int count, int cookTime,
                       float exp, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.cookTime = cookTime;
            this.exp = exp;
            this.ingredients = ingredients;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {

            Gson gson = new Gson();

            pJson.add("cookTime", gson.toJsonTree(this.cookTime));
            pJson.add("exp", gson.toJsonTree(this.exp));

            JsonArray array = new JsonArray();
            this.ingredients.forEach((ing) -> array.add((ing.toJson())));

            pJson.add("ingredients", array);

            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());

            if(this.count > 1) json.addProperty("count", this.count);

            pJson.add("result", json);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Bb.MODID,
                    ForgeRegistries.ITEMS.getKey(this.result).getPath() + "_from_crucible");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrucibleRecipe.Serializer.INSTANCE;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
