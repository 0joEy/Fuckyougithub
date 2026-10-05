package net.henrycmoss.bb.recipe;

import com.google.gson.JsonObject;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.entity.TubContainer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class TubRecipe implements Recipe<TubContainer> {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final FluidStack fluidInput;
    private final FluidStack fluidOutput;
    private final ItemStack itemOutput;
    private final int time;
    private final boolean isFluidRecipe;

    private final boolean cooked;

    public TubRecipe(ResourceLocation id, Ingredient ingredient, FluidStack fluidInput,
                     FluidStack fluidResult, ItemStack itemResult, int time, boolean cooked) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluidInput = fluidInput;
        this.itemOutput = itemResult;
        fluidOutput = fluidResult;
        this.time = time;
        isFluidRecipe = !fluidResult.isEmpty();
        this.cooked = cooked;
    }

    @Override
    public boolean matches(TubContainer pContainer, Level pLevel) {
        if (pContainer.getFluid().isEmpty() || this.fluidInput.isEmpty()) {
            return false;
        }
        boolean itemMatches = this.ingredient.test(pContainer.getItem(0));
        boolean fluidMatches = pContainer.getFluid().getFluid().isSame(this.fluidInput.getFluid())
                && pContainer.getFluid().getAmount() >= this.fluidInput.getAmount();

        return itemMatches && fluidMatches;
    }

    @Override
    public ItemStack assemble(TubContainer p_44001_, RegistryAccess p_267165_) {
        return this.itemOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return this.itemOutput;
    }

    public FluidStack getResultFluid() {
        return this.fluidOutput;
    }

    public ItemStack getResultItem() {
        return this.itemOutput;
    }

    public FluidStack getFluidInput() {
        return fluidInput;
    }

    public int getTime() {
        return time;
    }

    public boolean isCooked() {
        return cooked;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public boolean isFluidRecipe() {
        return isFluidRecipe;
    }

    public static class Type implements RecipeType<TubRecipe> {

        public static final TubRecipe.Type INSTANCE = new TubRecipe.Type();
        public static final ResourceLocation ID = new ResourceLocation(Bb.MODID, "tub");

    }

    public static class Serializer implements RecipeSerializer<TubRecipe> {
        public static final TubRecipe.Serializer INSTANCE = new TubRecipe.Serializer();

        @Override
        public TubRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));

            JsonObject fluidInJson = GsonHelper.getAsJsonObject(json, "fluid_input");
            Fluid fluidIn = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(fluidInJson, "fluid")));
            FluidStack inputFluid = new FluidStack(fluidIn, 1000);

            FluidStack fluidResult = FluidStack.EMPTY;
            ItemStack itemResult = ItemStack.EMPTY;
            if(GsonHelper.getAsJsonObject(json, "result").has("fluid")) {
                JsonObject fluidOutJson = GsonHelper.getAsJsonObject(json, "result");
                Fluid fluidOut = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(
                        GsonHelper.getAsString(fluidOutJson, "fluid")
                ));
                fluidResult = new FluidStack(fluidOut, 1000);
            }
            else {
                itemResult = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), false);
            }
            int time = json.get("time").getAsInt();
            boolean cooked = json.get("cooked").getAsBoolean();

            return new TubRecipe(recipeId, ingredient, inputFluid, fluidResult, itemResult, time, cooked);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TubRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidInput);
            buf.writeFluidStack(recipe.getResultFluid());
            buf.writeItem(recipe.getResultItem());
            buf.writeVarInt(recipe.getTime());
            buf.writeBoolean(recipe.isCooked());
        }

        @Override
        public @Nullable TubRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            FluidStack input = buf.readFluidStack();
            FluidStack fluidOutput = buf.readFluidStack();
            ItemStack itemOutput = buf.readItem();
            int time = buf.readVarInt();
            boolean cooked = buf.readBoolean();
            return new TubRecipe(id, ingredient, input, fluidOutput, itemOutput, time, cooked);
        }
    }

    @Override
    public RecipeType<?> getType() {
        return TubRecipe.Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TubRecipe.Serializer.INSTANCE;
    }
}
