package net.henrycmoss.bb.worldgen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class BbConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> BATTERY_KEY = registerKey("battery");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARIJUANA_KEY = registerKey("marijuana");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOMS = registerKey("shrooms");



    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        register(context, BATTERY_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BbBlocks.ETHER.get()),
                new StraightTrunkPlacer(3, 4, 3),
                BlockStateProvider.simple(BbBlocks.SULFUR_ORE.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(1, 0, 2)).build());

        register(context, MARIJUANA_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BbBlocks.MARIJUANA_BUSH.get())))));

        register(context, SHROOMS, Feature.FLOWER,
                new RandomPatchConfiguration(32, 6, 2,
                        PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new
                                SimpleBlockConfiguration(BlockStateProvider.simple(BbBlocks.SHROOM_PATCH.get())))));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Bb.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                         ResourceKey<ConfiguredFeature<?, ?>> key, F feature,
                                                                                         FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }


}
