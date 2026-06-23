package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.phys.BbMath;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Bb.MODID)
public class LavaCreeperListener {

    private static final Map<UUID, Integer> tickMap = new HashMap<>();
    private static final Map<UUID, Integer> maxTickMap = new HashMap<>();

    @SubscribeEvent
    public static void creeperTick(LivingEvent event) {
        if(event.getEntity() instanceof Creeper creeper && creeper.getTags().contains("lava")) {
            if(!tickMap.containsKey(creeper.getUUID())) {
                tickMap.put(creeper.getUUID(), 0);
                maxTickMap.put(creeper.getUUID(), 5);
            }
            increment(creeper.getUUID());
            if(tickMap.get(creeper.getUUID()) >= maxTickMap.get(creeper.getUUID())) {
                ServerLevel level = (ServerLevel) creeper.level();
                Random rand = new Random();
                double dX = rand.nextDouble(0d, 0.5d);
                double dZ = rand.nextDouble(0d, 0.5d);
                level.sendParticles(ParticleTypes.LAVA, creeper.getX(), creeper.getY(),
                        creeper.getZ(), 1, dX, 0d, dZ, 0d);
                tickMap.replace(creeper.getUUID(), 0);
                maxTickMap.replace(creeper.getUUID(), (int) BbMath.gaussian(new Random().nextDouble(2d, 15d),
                        20, 0.35d));
            }
        }
    }

    private static void increment(UUID id) {
        tickMap.replace(id, tickMap.get(id) + 1);
    }
}
