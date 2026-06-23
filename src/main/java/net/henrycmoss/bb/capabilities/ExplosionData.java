package net.henrycmoss.bb.capabilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ExplosionData implements IExplosion {

    private int remaining;
    private int max = 200;

    private boolean toggle;

    LivingEntity target;

    private final Vec3 startPos;

    public ExplosionData(LivingEntity target) {
        this.target = target;
        this.startPos = target.position();
        remaining = max;
        toggle = true;
    }

    @Override
    public int getTicks() {
        return remaining;
    }

    @Override
    public void setTicks(int ticks) {
        if(ticks > 0) remaining = ticks;
    }

    @Override
    public void decrement() {
        if(remaining > 0) remaining--;
    }

    @Override
    public LivingEntity getEntity() {
        return target;
    }

    @Override
    public boolean isOn() {
        return toggle;
    }

    @Override
    public void start() { toggle = true; }

    @Override
    public void done() {
        toggle = false;
        remaining = max;
    }

    @Override
    public Vec3 startPos() { return startPos; }
}
