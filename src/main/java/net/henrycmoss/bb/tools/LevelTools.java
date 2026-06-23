package net.henrycmoss.bb.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LevelTools {

    public static boolean fillBlocks(Level level, BlockPos startPos, int dX, int dY, int dZ, BlockState block) {
        int area = Math.abs(dX * dY * dZ);
        if(area == 0) return false;
        for(int i = 0; i < area; i++) {
            int x = i % dX;
            int y = (i / dX) % dY;
            int z = y / dY;
            level.setBlock(startPos.offset(x, y, z), block, 3);
        }
        return true;
    }

    public static boolean fillBlocksFromPoint(Level level, BlockState block, int x1, int x2,
                                              int y1, int y2, int z1, int z2) {
        return fillBlocks(level, new BlockPos(x1, y1, z1), (x2 - x1), (y2 - y1), (z2 - z1), block);
    }
}
