package net.henrycmoss.bb.structures;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class OtherStructureclassBro {

    private final Color color;
    private final ResourceKey<Structure> structure;
    private final ResourceKey<Level> dimension;
    private final Vec3i size;

    public OtherStructureclassBro(Color color, ResourceKey<Structure> structure, ResourceKey<Level> dimension,
                                  Vec3i size) {
        this.color = color;
        this.structure = structure;
        this.dimension = dimension;
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public ResourceKey<Structure> getStructure() {
        return structure;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public Vec3i getSize() {
        return size;
    }


}
