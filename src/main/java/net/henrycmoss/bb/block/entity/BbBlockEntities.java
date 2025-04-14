package net.henrycmoss.bb.block.entity;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bb.MODID);

    public static final RegistryObject<BlockEntityType<GemEmpoweringStationBlockEntity>> GEM_EMPOWERING_STATION_BE =
            BLOCK_ENTITIES.register("gem_empowering_station_block_entity", () ->
                    BlockEntityType.Builder.of(GemEmpoweringStationBlockEntity::new,
                            BbBlocks.GEM_EMPOWERING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE_BE = BLOCK_ENTITIES.register("crucible_block_entity",
            () -> BlockEntityType.Builder.of(CrucibleBlockEntity::new, BbBlocks.CRUCIBLE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}