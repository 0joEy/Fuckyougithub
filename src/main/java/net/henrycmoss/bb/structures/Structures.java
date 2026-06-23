package net.henrycmoss.bb.structures;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class Structures {

    public static final int[][][] test = new StructureBuilder(15, 10, 10)
            .fill(10, 5, 1, Block.getId(Blocks.STONE.defaultBlockState()))
            .addBlocks(Block.getId(Blocks.BAMBOO_BLOCK.defaultBlockState()),
            5).next().addBlocks(Block.getId(Blocks.DIAMOND_BLOCK.defaultBlockState()), 15).nextLayer()
            .next().addBlocks(Block.getId(Blocks.NETHER_BRICKS.defaultBlockState()), 17)
            .fill(3, 5, 5, Block.getId(Blocks.OAK_PLANKS.defaultBlockState())).build();

    public static final int[][][] LAVA_PIT = new StructureBuilder(9, 9, 20)
            .fill(9, 9, 1, Blocks.LAVA).fill(9, 9, 2, Blocks.COBWEB)
            .fill(9, 9, 18, Blocks.AIR).build();
}
