package net.henrycmoss.bb.datagen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.worldgen.BbBiomeModifiers;
import net.henrycmoss.bb.worldgen.BbPlacedFeatures;
import net.henrycmoss.bb.worldgen.BbConfiguredFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BbWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, BbConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, BbPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BbBiomeModifiers::bootstrap);


    public BbWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Bb.MODID));
    }
}
