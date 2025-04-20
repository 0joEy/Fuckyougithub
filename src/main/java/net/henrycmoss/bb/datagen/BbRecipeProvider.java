package net.henrycmoss.bb.datagen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.datagen.custom.CrucibleRecipeBuilder;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.recipe.CrucibleRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class BbRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final List<ItemLike> SMELTABLES = List.of(BbItems.UNLIT_JOINT.get(),
            BbItems.GRAPHITE.get(), BbBlocks.SULFUR_ORE.get(), BbBlocks.ETHER.get(),
            BbBlocks.COCAINE_TRAY.get());

    public BbRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, SMELTABLES, RecipeCategory.MISC, BbItems.METHAMPHETAMINE.get(), 0.25f, 200, "methamphetamine");
        oreBlasting(pWriter, SMELTABLES, RecipeCategory.MISC, BbItems.METHAMPHETAMINE.get(), 0.25f, 100, "methamphetamine");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BbBlocks.ETHER.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', BbItems.UNLIT_JOINT.get())
                .unlockedBy(getHasName(BbItems.UNLIT_JOINT.get()), 
                        has(BbItems.UNLIT_JOINT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BbItems.UNLIT_JOINT.get(), 9)
                .requires(BbBlocks.ETHER.get().asItem())
                .unlockedBy(getHasName(BbBlocks.ETHER.get().asItem()),
                        has(BbBlocks.ETHER.get().asItem()))
                .save(pWriter);
        
        /*ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BbItems.GRAPHITE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BbItems.BATTERY.get())
                .pattern("#G#")
                .pattern("WGW")
                .pattern("#G#")
                .define('#', Items.IRON_INGOT).define('W', Items.WATER_BUCKET)
                .define('G', BbItems.GRAPHITE.get()).unlockedBy("has_graphite",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BbItems.GRAPHITE.get()).build()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BbBlocks.COCAINE_TRAY.get().asItem())
                        .pattern("###").pattern("###").pattern("###").define('#', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                        .unlockedBy("hit_by_lightning", EntityHurtPlayerTrigger.TriggerInstance
                                .entityHurtPlayer(DamagePredicate.Builder.damageInstance()
                                        .sourceEntity(EntityPredicate.Builder.entity().of(EntityType.LIGHTNING_BOLT).build())));

        oreSmelting(pWriter, List.of(Items.COAL), RecipeCategory.MISC, BbItems.GRAPHITE.get(),
                1.2f,
            150, "graphite");

        new CrucibleRecipeBuilder(NonNullList.of(new ItemStack(BbItems.HYDROGEN_GAS.get()),
                new ItemStack(BbItems.CHLORINE_GAS.get())), BbItems.HCL.get(), 1, 150, 1.5f)
                .unlockedBy("has_chlorine_gas", has(BbItems.CHLORINE_GAS.get())).save(pWriter);*/
    }

    /*private static void oreSmelting(Consumer<FinishedRecipe> finishedRecipe, RecipeSerializer<? extends AbstractCookingRecipe> serializer, List<ItemLike> ingredients,
                                    ItemLike result, int cookTime, float exp, RecipeCategory category, String group, String recipeName) {
        for(ItemLike item : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(item), category, result, exp, cookTime, serializer)
                    .group(group).unlockedBy(getHasName(item), has(item)).save(finishedRecipe,
                            Bb.MODID + ":" + getItemName(result) + recipeName + "_" + getItemName(item));
        }*/

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer,  Bb.MODID
                            + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
