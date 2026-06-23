package net.henrycmoss.bb.phys;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class CircleGenerator {

    public static List<BlockPos> circle(int r, BlockPos center) {
        int d = r * 2;
        List<BlockPos> circle = new ArrayList<>();

        int n = (int)(d * Math.PI);

        for(int i = 0; i < n; i++) {
            double segment = (2 * Math.PI) / n;
            float dX = (float)(r * Math.cos(segment * i)) * 0.95f;
            float dY = (float)(r * Math.sin(segment * i)) * 0.95f;
            circle.add(new BlockPos(center.getX() + Math.round(dX),
                    center.getY() + Math.round(dY), center.getZ()));
            LogUtils.getLogger().info("BLOCK " + (i + 1) + ";  X offset: " + dX + "\nY offset: " + dY);
        }
        return circle;
    }
}
