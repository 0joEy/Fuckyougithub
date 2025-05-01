package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.Bb;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            Bb.MODID);

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> CRUCIBLE = SERIALIZERS.register("crucible", () -> CrucibleRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ElectrolyticCellRecipe>> ELECTROLYTIC_CELL = SERIALIZERS.register("electrolytic_cell",
            () -> ElectrolyticCellRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
