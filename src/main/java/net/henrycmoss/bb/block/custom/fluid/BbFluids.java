package net.henrycmoss.bb.block.custom.fluid;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbFluids {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Bb.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_ACID = FLUIDS.register("acid_fluid",
            () -> new ForgeFlowingFluid.Source(BbFluids.ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACID = FLUIDS.register("flowing_acid",
            () -> new ForgeFlowingFluid.Flowing(BbFluids.ACID_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BbFluidTypes.ACID_FLUID_TYPE, SOURCE_ACID, FLOWING_ACID).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(BbBlocks.ACID).bucket(BbItems.SULFURIC_ACID_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
