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

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Bb.MODID);

    // Common overlay used by all fluids (or null if not needed)
    public static final ResourceLocation BUBBLE_OVERLAY = new ResourceLocation(Bb.MODID, "block/acid_bubbles");
    public static final ResourceLocation GASOLINE_OVERLAY = new ResourceLocation(Bb.MODID, "block/gasoline_overlay");

    // 1. Acid Fluid
    public static final RegistryObject<FluidType> ACID_FLUID_TYPE = register(
            "acid",
            FluidType.Properties.create().viscosity(8).lightLevel(1).density(20)
                    .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK),
            0xFFFFFFFF,
            new Vector3f(216f / 255f, 240f / 255f, 10f / 255f),
            BUBBLE_OVERLAY
    );

    // 2. Gasoline Fluid
    public static final RegistryObject<FluidType> GASOLINE_FLUID_TYPE = register(
            "gasoline",
            FluidType.Properties.create().viscosity(5).lightLevel(0).density(10)
                    .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK),
            0xFF228B22,
            new Vector3f(240f / 255f, 200f / 255f, 50f / 255f),
            GASOLINE_OVERLAY
    );

    public static final RegistryObject<FluidType> COCA_PASTE_FLUID_TYPE = register(
            "coca_paste",
            FluidType.Properties.create().viscosity(5).lightLevel(0).density(10)
                    .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK),
            0xFFFFFFFF,
            new Vector3f(240f / 255f, 200f / 255f, 50f / 255f),
            GASOLINE_OVERLAY
    );

    /**
     * Automatically maps textures to:
     * - still:   "bb:block/<name>_still"
     * - flowing: "bb:block/<name>_flow"
     */
    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties, int tintColor, Vector3f fogColor, ResourceLocation overlay) {
        ResourceLocation still = new ResourceLocation(Bb.MODID, "block/" + name + "_still");
        ResourceLocation flowing = new ResourceLocation(Bb.MODID, "block/" + name + "_flow");

        return FLUID_TYPES.register(name + "_fluid", () -> new BaseFluidType(
                still,
                flowing,
                overlay,
                tintColor,
                fogColor,
                properties
        ));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
