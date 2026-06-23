package net.henrycmoss.bb.capabilities.provider;

import net.henrycmoss.bb.capabilities.BbCapabilities;
import net.henrycmoss.bb.capabilities.ExplosionData;
import net.henrycmoss.bb.capabilities.IExplosion;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ExplosionProvider implements ICapabilityProvider {

    private LivingEntity entity;

    public ExplosionProvider(LivingEntity entity) {
        this.entity = entity;
    }

    private final ExplosionData data =
            new ExplosionData(entity);

    private final LazyOptional<IExplosion>
            optional =
            LazyOptional.of(() -> data);

    @Override
    public <T> LazyOptional<T> getCapability(
            Capability<T> cap,
            Direction side)
    {
        return cap ==
                BbCapabilities.EXPLOSION

                ? optional.cast()
                : LazyOptional.empty();
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
