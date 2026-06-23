package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Bb.MODID)
public class MagicPigListener {

    private static final Map<Pig, Pig> passengerMap = new HashMap<>();

    @SubscribeEvent
    public static void playerHurtMagicPig(AttackEntityEvent event) {
        if(event.getTarget() instanceof Pig pig && pig.getTags().contains("magic")) {
            Level level = pig.level();
            Pig passenger = EntityType.PIG.create(level);
            level.addFreshEntity(passenger);
            pig.getPassengers().add(passenger);
        }
        else event.getTarget().addTag("magic");
    }
}
