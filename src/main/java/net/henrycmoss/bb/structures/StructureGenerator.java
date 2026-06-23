package net.henrycmoss.bb.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StructureGenerator {

    public static void generateStructure(int[][][] structure, BlockPos startPos, Level level) {
        for(int y = 0; y < structure.length; y++) {
            for(int z = 0; z < structure[y].length; z++) {
                for(int x = 0; x < structure[y][z].length; x++) {
                    level.setBlock(startPos.offset(x, y, z), Block.stateById(structure[y][z][x]), 3);
                }
            }
        }
    }

    public static void generateStructureCentered(int[][][] structure, BlockPos pos, Level level) {
        pos = pos.offset((int) -(structure[0][0].length / 2), (int) (-structure.length / 2),
                (int) -(structure[0].length / 2));
        generateStructure(structure, pos, level);
    }
}
