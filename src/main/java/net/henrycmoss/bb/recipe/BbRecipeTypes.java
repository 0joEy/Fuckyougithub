package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.Bb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BbRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Bb.MODID);

    public static final RegistryObject<RecipeType<CrucibleRecipe>> CRUCIBLE = registerType(CrucibleRecipe.Type.ID, () -> CrucibleRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<ElectrolysisRecipe>> ELECTROLYSIS =
            registerType(ElectrolysisRecipe.Type.ID, () -> ElectrolysisRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<TestRecipe>> TEST =
            registerType(TestRecipe.Type.ID, () -> TestRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<JarRecipe>> JAR =
            registerType(JarRecipe.Type.ID, () -> JarRecipe.Type.INSTANCE);

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(ResourceLocation id, Supplier<RecipeType<T>> type) {
        TYPES.createTagKey(id.getPath());
        TYPES.createTagKey(id);
        return TYPES.register(id.getPath(), type);
    }




    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
