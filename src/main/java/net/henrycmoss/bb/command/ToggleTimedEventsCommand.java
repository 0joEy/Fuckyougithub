package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import net.henrycmoss.bb.events.listeners.TimedEventListener;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class ToggleTimedEventsCommand {

    public ToggleTimedEventsCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("toggle_event_timer").executes(source -> toggle(source.getSource(), source.getSource().getPlayer())));
    }

    private static int toggle(CommandSourceStack source, Player player) {
        TimedEventListener.run = !TimedEventListener.run;
        if(player != null) {
            String s = TimedEventListener.run ? "on" : "off";
            player.sendSystemMessage(Component.literal("Random timed events are now turned " + s + "."));
        }
        return 1;
    }
}
