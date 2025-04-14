package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class FlyCommand {

    public FlyCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("fly").executes(source -> fly(source.getSource(), source.getSource().getPlayer())));
    }

    private static int fly(CommandSourceStack source, Player player) {
        if(player != null) {
            player.getAbilities().mayfly = !player.getAbilities().mayfly;
            player.onUpdateAbilities();
            source.sendSuccess(() -> Component.literal("Sucess"), true);
            return 1;
        }
        source.sendFailure(Component.literal("Cannot find player"));
        return 0;
    }
}
