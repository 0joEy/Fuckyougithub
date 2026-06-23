package net.henrycmoss.bb.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.awt.*;
import java.util.UUID;

public class StructureZone {
    private final UUID id;

    private final ResourceKey<Level> dimension;
    private final ResourceKey<Structure> structure;

    private final BoundingBox bounds;

    private boolean bossKilled;
    private int mobsRemaining;

    public StructureZone(ResourceKey<Level> dimension, ResourceKey<Structure> structure,
                         BoundingBox bounds) {
        this.id = UUID.randomUUID();
        this.dimension = dimension;
        this.structure = structure;
        this.bounds = bounds;
    }

    public boolean contains(BlockPos pos) {
        return this.bounds.isInside(pos);
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    public UUID getId() {
        return id;
    }

    public ResourceKey<Structure> getStructure() {
        return structure;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }
}
