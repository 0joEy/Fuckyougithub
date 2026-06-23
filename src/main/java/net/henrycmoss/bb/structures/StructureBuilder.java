package net.henrycmoss.bb.structures;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StructureBuilder {

    private final int[][][] structure;

    private int cursor = 0;

    private final int sizeY, sizeZ, sizeX;
    private final int max;

    public StructureBuilder(int sizeX, int sizeZ, int sizeY) {
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.sizeX = sizeX;
        max = sizeX * sizeZ * sizeY;
        structure = new int[this.sizeY][this.sizeZ][this.sizeX];
    }

    public StructureBuilder addBlock(int blockId) {
        if(cursor <= max) {
            structure[z()][y()][x()] = blockId;
            cursor++;
        }
        return this;
    }

    public StructureBuilder addBlock(Block block) {
        return addBlock(Block.getId(block.defaultBlockState()));
    }

    public StructureBuilder addBlocks(int blockId, int amount) {
        for(int i = 0; i < amount; i++) {
            addBlock(blockId);
        }

        return this;
    }

    public StructureBuilder addBlocks(Block block, int amount) {
        return addBlocks(Block.getId(block.defaultBlockState()), amount);
    }

    public StructureBuilder next() {
        cursor += sizeX - x() - 1;
        return this;
    }

    public StructureBuilder nextLayer() {
        cursor += ((sizeZ - z() - 1) * sizeX) - x() - 1;
        return this;
    }

    public StructureBuilder fill(int dX, int dZ, int dY, int blockId) {
        if(dX <= sizeX - x() && dY <= sizeY - y() && dZ <= sizeZ - z()) {
            int pX, pY, pZ;
            for (int i = 0; i < dX * dZ * dY; i++) {
                pX = i % dX;
                pZ = (i / dX) % dZ;
                pY = i / dX / dZ;
                structure[y() + pY][z() + pZ][x() + pX] = blockId;
                LogUtils.getLogger().info(String.format("{%d}, {%d}, {%d}", pX,  pZ, y() + pY));
            }

            int bottom = dZ * sizeX - x() - 1;
            int top = dY > 1 ? (z() + dZ) * sizeX : 0;
            int middle = dY > 2 ? (dY - 2) * sizeZ * sizeX : 0;
            cursor += bottom + middle + top;
        }
        return this;
    }

    public StructureBuilder fill(int dX, int dZ, int dY, Block block) {
        return fill(dX, dY, dZ, Block.getId(block.defaultBlockState()));
    }

    public int[][][] build() {
        return structure;
    }

    private int z() { return cursor / sizeX % sizeZ; }
    private int y() {return cursor / sizeX / sizeZ; }
    private int x() { return cursor % sizeX; }
}
