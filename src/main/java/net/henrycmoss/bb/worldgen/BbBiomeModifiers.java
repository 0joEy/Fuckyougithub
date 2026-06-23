package net.henrycmoss.bb.worldgen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.BbEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class BbBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_MARIJUANA = registerKey("add_marijuana");

    public static final ResourceKey<BiomeModifier> ADD_SHROOMS = registerKey("add_shrooms");

    public static final ResourceKey<BiomeModifier> ADD_EVIL_PIGS = registerKey("add_evil_pigs");

    public static final ResourceKey<BiomeModifier> ADD_IRS_AGENTS = registerKey("add_irs_agents");

    public static final ResourceKey<BiomeModifier> ADD_POLICE_OFFICERS = registerKey("add_police_officers");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_MARIJUANA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BbPlacedFeatures.MARIJUANA_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_SHROOMS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(BbPlacedFeatures.SHROOMS_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_EVIL_PIGS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                List.of(new MobSpawnSettings.SpawnerData(BbEntities.EVIL_PIG.get(), 30, 3, 15))
        ));

        context.register(ADD_IRS_AGENTS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS),
                        biomes.getOrThrow(Biomes.DESERT)),
                List.of(new MobSpawnSettings.SpawnerData(BbEntities.IRS_AGENT.get(), 20, 1, 3))
        ));

        context.register(ADD_POLICE_OFFICERS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_DRY_OVERWORLD),
                List.of(new MobSpawnSettings.SpawnerData(BbEntities.IRS_AGENT.get(), 30, 3, 15))
        ));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Bb.MODID, name));
    }
}
