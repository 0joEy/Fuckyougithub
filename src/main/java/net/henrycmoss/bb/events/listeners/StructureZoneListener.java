package net.henrycmoss.bb.events.listeners;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.ZoneAnchorBlock;
import net.henrycmoss.bb.datagen.ZoneSavedData;
import net.henrycmoss.bb.structures.BbStructures;
import net.henrycmoss.bb.structures.StructureZone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StructureZoneListener {

    public static void onChunkLoad(ChunkEvent.Load event) {
        List<BlockPos> positions = new ArrayList<>();
        event.getChunk().findBlocks(
                (state) -> (state.is(BbBlocks.ZONE_ANCHOR.get()) &&
                        state.hasProperty(ZoneAnchorBlock.STRUCTURE)), (pos, state) -> positions.add(pos));
        ZoneSavedData data = new ZoneSavedData();
        for(BlockPos pos : positions) {
            BlockState state = event.getLevel().getBlockState(pos);
            int type = state.getValue(ZoneAnchorBlock.STRUCTURE);
            switch (type) {
                case 0 -> data.addZone(new StructureZone(BbStructures.POLICE_STATION.getDimension(),
                        BbStructures.POLICE_STATION.getStructure(), BoundingBox.fromCorners(pos,
                        pos.offset(BbStructures.POLICE_STATION.getSize()))));
            }
            LogUtils.getLogger().info(data.findZone(ServerLevel.OVERWORLD, pos).get().getId().toString());
        }
    }
}