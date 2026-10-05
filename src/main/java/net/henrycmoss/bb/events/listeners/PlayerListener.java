package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerListener {
    @SubscribeEvent
    public static void playerHurtEntity(LivingAttackEvent event) {
        if(event.getSource().getEntity() instanceof Player player) {
            Level level = player.level();
            if(level.getRandom().nextFloat() <= 0.075f) {
                player.setDeltaMovement(player.getDeltaMovement().add(
                        event.getEntity().position().subtract(player.position()).normalize().scale(5)
                                .add(0, 2, 0)));
            }
        }
    }
}
