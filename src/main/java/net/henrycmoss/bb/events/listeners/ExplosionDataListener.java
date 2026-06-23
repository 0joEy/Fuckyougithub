package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.capabilities.BbCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID)
public class ExplosionDataListener {


    @SubscribeEvent
    public static void tick(LivingEvent.LivingTickEvent event) {
        event.getEntity().getCapability(BbCapabilities.EXPLOSION).ifPresent(cap -> {
            if (cap.isOn()) {
                Level level = event.getEntity().level();
                LivingEntity target = event.getEntity();
                double distance = 20;
                cap.decrement();
                Vec3 pos = target.position();
                target.moveTo(pos.add(0d, 0.05d * distance, 0d));
                if (cap.getTicks() <= 0) {
                    level.explode(target, pos.x, pos.y, pos.z,
                            5f, false, Level.ExplosionInteraction.BLOCK);
                    Vec3i center = new Vec3i(Math.round((float) pos.x),
                            Math.round((float) pos.y), Math.round((float) pos.z));

                    for (int x = -1; x < 1; x++) {
                        for (int y = 0; y < 1; y++) {
                            FallingBlockEntity.fall(level,
                                    (BlockPos) center.offset(x, y, 0),
                                    Blocks.REDSTONE_BLOCK.defaultBlockState());
                        }
                    }

                    cap.done();
                }
            }
        });
    }
}
