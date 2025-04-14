package net.henrycmoss.bb.effect;

import net.henrycmoss.bb.Bb;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            Bb.MODID);

    public static final RegistryObject<MobEffect> HALLUCINATION = EFFECTS.register("hallucination",
            () -> new HallucinationEffect(MobEffectCategory.HARMFUL, 0xFF0000));

    public static final RegistryObject<MobEffect> BAD_TRIP = EFFECTS.register("bad_trip",
            () -> new BadTripEffect(MobEffectCategory.HARMFUL, 0x000000));

    public static final RegistryObject<MobEffect> ACID_BURN = EFFECTS.register("acid_burn",
            () -> new AcidBurnEffect(MobEffectCategory.HARMFUL, 0xB0DE0BFF));

    public static final RegistryObject<MobEffect> SHROOMS = EFFECTS.register("shrooms",
            () -> new ShroomsEffect(MobEffectCategory.NEUTRAL, 0x5b040d));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
