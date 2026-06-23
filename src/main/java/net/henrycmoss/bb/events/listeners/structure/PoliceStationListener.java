package net.henrycmoss.bb.events.listeners.structure;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.datagen.ZoneSavedData;
import net.henrycmoss.bb.structures.BbStructures;
import net.henrycmoss.bb.structures.StructureZone;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PoliceStationListener {

    public static void blockBroken(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();
        if(!level.isClientSide()) {
            ZoneSavedData data = ZoneSavedData.get((ServerLevel) level);
            Optional<StructureZone> zone = data.findZone(level.dimension(), event.getPos());
            if(zone.isPresent() && zone.get().getStructure().equals(BbStructures.POLICE_STATION.getStructure())
                && level.getBlockState(event.getPos()).is(Tags.Blocks.NETHERRACK)) {
                player.kill();
            }
        }
    }
}
