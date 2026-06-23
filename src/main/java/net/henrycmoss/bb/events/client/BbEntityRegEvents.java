package net.henrycmoss.bb.events.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.BbEntities;
import net.henrycmoss.bb.entity.client.EvilPigModel;
import net.henrycmoss.bb.entity.client.IRSAgentModel;
import net.henrycmoss.bb.entity.client.PoliceOfficerModel;
import net.henrycmoss.bb.entity.layers.BbModelLayers;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BbEntityRegEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BbModelLayers.PIG_LAYER, EvilPigModel::createBodyLayer);
        event.registerLayerDefinition(BbModelLayers.AGENT_LAYER, IRSAgentModel::createLayers);
        event.registerLayerDefinition(BbModelLayers.OFFICER_LAYER, PoliceOfficerModel::createBodyLayer);
    }



    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(BbEntities.EVIL_PIG.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BbEntities.IRS_AGENT.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(BbEntities.POLICE_OFFICER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
