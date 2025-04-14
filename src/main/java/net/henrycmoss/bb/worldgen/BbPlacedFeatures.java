package net.henrycmoss.bb.worldgen;

import net.henrycmoss.bb.Bb;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BbPlacedFeatures {

    public static final ResourceKey<PlacedFeature> BATTERY_PLACED_KEY = registerKey("battery_placed");

    public static final ResourceKey<PlacedFeature> MARIJUANA_PLACED_KEY = registerKey("marijuana_placed");

    public static final ResourceKey<PlacedFeature> SHROOMS_PLACED_KEY = registerKey("shrooms_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, BATTERY_PLACED_KEY, configuredFeatures.getOrThrow(BbConfiguredFeatures.BATTERY_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2)));

        register(context, MARIJUANA_PLACED_KEY, configuredFeatures.getOrThrow(BbConfiguredFeatures.MARIJUANA_KEY),
                List.of(RarityFilter.onAverageOnceEvery(18), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, SHROOMS_PLACED_KEY, configuredFeatures.getOrThrow(BbConfiguredFeatures.SHROOMS),
                List.of(RarityFilter.onAverageOnceEvery(18), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Bb.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }


}
