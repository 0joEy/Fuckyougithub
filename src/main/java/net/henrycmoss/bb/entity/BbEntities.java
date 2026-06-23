package net.henrycmoss.bb.entity;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.client.IRSAgentRenderer;
import net.henrycmoss.bb.entity.custom.EvilPigEntity;
import net.henrycmoss.bb.entity.custom.IRSAgentEntity;
import net.henrycmoss.bb.entity.custom.PoliceOfficerEntity;
import net.henrycmoss.bb.entity.custom.projectile.AbstractMissile;
import net.henrycmoss.bb.entity.custom.projectile.Missile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bb.MODID);

    public static final RegistryObject<EntityType<EvilPigEntity>> EVIL_PIG =
            ENTITY_TYPES.register("evil_pig", () -> EntityType.Builder.of(EvilPigEntity::new,
                    MobCategory.AMBIENT).sized(1f, 2f).build("evil_pig"));

    public static final RegistryObject<EntityType<IRSAgentEntity>> IRS_AGENT =
            ENTITY_TYPES.register("irs_agent", () ->
                    EntityType.Builder.of(IRSAgentEntity::new, MobCategory.CREATURE).sized(1f, 2f)
                            .build("irs_agent"));

    public static final RegistryObject<EntityType<PoliceOfficerEntity>> POLICE_OFFICER =
            ENTITY_TYPES.register("police_officer", () ->
                    EntityType.Builder.of(PoliceOfficerEntity::new, MobCategory.MISC)
                            .sized(1f, 2f).build("police_officer"));

    public static final RegistryObject<EntityType<Missile>> MISSILE =
            ENTITY_TYPES.register("missile", () ->
                    EntityType.Builder.of(Missile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).clientTrackingRange(4)
                            .updateInterval(10).build("missile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
