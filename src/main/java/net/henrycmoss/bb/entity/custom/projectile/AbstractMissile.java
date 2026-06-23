package net.henrycmoss.bb.entity.custom.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractMissile extends Projectile {

    private boolean inGround;

    protected AbstractMissile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        Vec3 v = this.position().add(this.getDeltaMovement());
        Vec3 particlePos = this.position().subtract(v.normalize().scale(0.5f));
        level().addAlwaysVisibleParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y,
                particlePos.z, 0d, 0d, 0d);

        double scale = 1d;
        BlockPos blockPos = this.blockPosition();
        BlockState block = this.level().getBlockState(blockPos);
        if(!block.isAir()) {
            VoxelShape collider = block.getCollisionShape(this.level(), blockPos);
            if(!collider.isEmpty()) {
                Vec3 pos = this.position();

                for(AABB area : collider.toAabbs()) {
                    if(area.move(blockPos).contains(pos)) {
                        this.inGround = true;
                        break;
                    }
                }

                if(block.is(Blocks.WATER)) scale = 0.3d;
                else {
                    this.level().explode(this, pos.x, pos.y, pos.z,
                            5f, true, Level.ExplosionInteraction.TNT);
                    this.kill();
                }
            }
        }

        Vec3 vec3 = this.getDeltaMovement();

        double f = 0.99d;

        this.setDeltaMovement(vec3.scale(f));

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
    }
}
