package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.Bb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BbRecipeTypes {

    private static final List<RecipeType<?>> types = new ArrayList<>();

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Bb.MODID);

    public static final RegistryObject<RecipeType<CrucibleRecipe>> CRUCIBLE = registerType(CrucibleRecipe.Type.ID.getPath(), () -> CrucibleRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<ElectrolyticCellRecipe>> ELECTROLYTIC_CELL = registerType(ElectrolyticCellRecipe.Type.ID.getPath(),
            () -> ElectrolyticCellRecipe.Type.INSTANCE);

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(String path, Supplier<RecipeType<T>> type) {
        TYPES.createTagKey(path);
        TYPES.createTagKey(path);
        types.add(type.get());
        return TYPES.register(path, type);
    }




    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
