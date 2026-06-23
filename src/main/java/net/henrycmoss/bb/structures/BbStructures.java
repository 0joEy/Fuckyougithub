package net.henrycmoss.bb.structures;

import net.henrycmoss.bb.Bb;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.core.jmx.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BbStructures {

    public static final List<OtherStructureclassBro> structures = new ArrayList<>();

    public static final OtherStructureclassBro POLICE_STATION = register("police_station",
            Color.BLUE, ServerLevel.OVERWORLD, new Vec3i(5, 5, 5));

    public static final OtherStructureclassBro test = register("police_station_2",
            Color.RED, ServerLevel.OVERWORLD, new Vec3i(3, 3, 3));

    public static OtherStructureclassBro register(String name, Color color,
                                                  ResourceKey<Level> dimension, Vec3i size) {
        OtherStructureclassBro structure = new OtherStructureclassBro(color,
                ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(Bb.MODID, name)),
                dimension, size);
        structures.add(structure);
        return structure;
    }
}
