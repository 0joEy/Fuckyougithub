package net.henrycmoss.bb.block.custom.fluid;

import net.henrycmoss.bb.Bb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jline.utils.Colors;
import org.joml.Vector3f;

import java.awt.*;

public class BbFluidTypes {

    public static final ResourceLocation ACID_STILL_RL = new ResourceLocation(Bb.MODID, "block/acid_still");
    public static final ResourceLocation ACID_FLOWING_RL = new ResourceLocation(Bb.MODID,"block/acid_flow");
    public static final ResourceLocation BUBBLE_OVERLAY = new ResourceLocation(Bb.MODID,"block/acid_bubbles");

    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Bb.MODID);

    public static final RegistryObject<FluidType> ACID_FLUID_TYPE = register("acid_fluid",
                    FluidType.Properties.create().viscosity(8).lightLevel(1).density(20).sound(SoundAction.get("drink"),
                            SoundEvents.HONEY_DRINK), 0xFFFFFFFF, new Vector3f(216f / 255f, 240f / 255f, 10f / 255f));

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties, int tintColor, Vector3f rgb) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(ACID_STILL_RL, ACID_FLOWING_RL, BUBBLE_OVERLAY,  tintColor,
                rgb, properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
