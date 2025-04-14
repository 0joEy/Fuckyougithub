package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import net.henrycmoss.bb.Bb;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class HomeCommand {

    private static BlockPos homePos = null;

    public HomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home").requires((player) -> player.hasPermission(2))
                .then(Commands.literal("set").executes((source) ->
                        setHome(source.getSource(), new BlockPos(new Vec3i((int) source.getSource().getPosition().x,
                                (int) source.getSource().getPosition().y, (int) source.getSource().getPosition().z)), source.getSource().getPlayer())).
                        then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((source) ->
                                setHome(source.getSource(), BlockPosArgument.getSpawnablePos(source, "pos"),
                                        source.getSource().getPlayer())))).then(Commands.literal("return").
                        executes((source) ->
                        returnHome(source.getSource(), homePos, source.getSource().getPlayer())))).createBuilder();
    }

    private static int setHome(CommandSourceStack source, BlockPos pos, Player player) {
        BlockPos pPos = player.blockPosition();
        player.getPersistentData().putIntArray(Bb.MODID + ":homepos", new int[]{ pPos.getX(), pPos.getY(), pPos.getZ() });
        source.sendSuccess(() -> Component.literal("Home position set at: " + pos.toShortString()), true);
        return 1;
    }

    private static int returnHome(CommandSourceStack source, BlockPos pos, Player player) {
        boolean hasHomePos = player.getPersistentData().getIntArray(Bb.MODID + ":homepos").length != 0;

        if(hasHomePos) {
            int[] pPos = player.getPersistentData().getIntArray(Bb.MODID + ":homepos");
            player.teleportTo(pPos[0], pPos[1], pPos[2]);
            source.sendSuccess(() -> Component.literal("Teleported " + player.getScoreboardName() + " to " +
                    pos.toShortString()), true);
            return 1;
        }
        else {
            source.sendFailure(Component.literal("Home has not been set"));
            return -1;
        }
    }
}
