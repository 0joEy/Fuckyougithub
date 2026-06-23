package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class CopyCommand {

    public CopyCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("copy").then(Commands.argument("pos1",
                BlockPosArgument.blockPos())).then(Commands.argument("pos2", BlockPosArgument.blockPos())
                .executes((source) -> copy(source.getSource(),
                        BlockPosArgument.getBlockPos(source, "pos1"),
                        BlockPosArgument.getBlockPos(source, "pos2"))))).createBuilder();
    }

    public int copy(CommandSourceStack source, BlockPos start, BlockPos end) {
        int dX = Math.abs(end.getX() - start.getX());
        int dZ = Math.abs(end.getZ() - start.getZ());
        int dY = Math.abs(end.getY() - start.getY());

        int[][][] structure = new int[dY][dZ][dX];
        Level level = source.getLevel();
        for(int y = 0; y < dY; y++) {
            for(int z = 0; z < dZ; z++) {
                for(int x = 0; x < dX; x++) {
                    structure[y][z][x] = Block.getId(level.getBlockState(start.offset(x, y, z)));
                }
            }
        }
        return 1;
    }
}
