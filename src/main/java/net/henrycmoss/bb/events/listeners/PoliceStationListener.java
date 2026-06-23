package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PoliceStationListener {

    @SubscribeEvent
    public static void blockBroken(BlockEvent.BreakEvent event) {
        if(!event.getLevel().isClientSide()) {
            ServerLevel level = event.getPlayer().getServer().getLevel(event.getPlayer().level().dimension());
            StructureManager manager = level.structureManager();
        }
    }
}
