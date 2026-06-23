package net.henrycmoss.bb.capabilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface IExplosion extends ITimer {

    LivingEntity getEntity();
    boolean isOn();
    void start();
    Vec3 startPos();
}
