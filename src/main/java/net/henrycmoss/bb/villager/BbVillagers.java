package net.henrycmoss.bb.villager;

import com.google.common.collect.ImmutableSet;
import net.henrycmoss.bb.Bb;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class BbVillagers {

    public static final DeferredRegister<PoiType> POI_TYPE = DeferredRegister.create(ForgeRegistries.POI_TYPES,
            Bb.MODID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(
            ForgeRegistries.VILLAGER_PROFESSIONS, Bb.MODID);

    public static final RegistryObject<PoiType> FURNACE_BLOCK_POI = POI_TYPE.register("furnace_block_poi",
            () -> new PoiType(ImmutableSet.copyOf(Blocks.FURNACE.getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> METH_COOK = VILLAGER_PROFESSION.register("meth_cook",
            () -> new VillagerProfession("meth_cook", x -> x.get() == FURNACE_BLOCK_POI.get(),
                    x -> x.get() == FURNACE_BLOCK_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_BUTCHER));

    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, FURNACE_BLOCK_POI);
        } catch(InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPE.register(eventBus);
        VILLAGER_PROFESSION.register(eventBus);
    }
}
