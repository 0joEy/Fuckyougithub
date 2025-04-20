package net.henrycmoss.bb.events;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TntCannonListener {

    @SubscribeEvent
    public static void tntCollide(EntityEvent event) {
        if(event.getEntity() instanceof PrimedTnt tnt && tnt.getTags().contains("cannon")) {
            AABB area = AABB.ofSize(tnt.position(), 6, 6, 6);

            List<Entity> es = tnt.level().getEntities(tnt, area, entity -> true);

            for(Entity e : es) {
                if(e instanceof Player || e instanceof PrimedTnt || e instanceof ItemEntity) {
                    LogUtils.getLogger().info(e.getType().getDescriptionId());
                }
                else if((!es.isEmpty() && e instanceof LivingEntity)){
                    tnt.setFuse(1);
                    LogUtils.getLogger().info(e.getType().getDescriptionId());
                    LogUtils.getLogger().info("blew");
                }
            }
        }
    }

}
