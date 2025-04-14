package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class IgniteCommand {

    public IgniteCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("ignite").then(Commands.argument("size", IntegerArgumentType.integer()).executes((source) ->
                ignite(source.getSource(), source.getSource().getPlayer(), IntegerArgumentType.getInteger(source, "size"))
        ))).createBuilder();
    }

    private int ignite(CommandSourceStack source, Player player, int size) {
        Vec3 pos = player.position();

        AABB area = AABB.ofSize(pos, size, size, size);

        List<Creeper> creepers = player.level().getEntitiesOfClass(Creeper.class, area, Objects::nonNull);

        if (player != null) {
            if (!creepers.isEmpty()) {
                int cs = 0;
                for (Creeper c : creepers) {
                    c.ignite();
                    LogUtils.getLogger().info("Ignited " + c.getName());
                    cs++;
                }
                int finalCs = cs;
                source.sendSuccess(() -> Component.literal("Ignited " + finalCs + " creepers"), true);
                return 1;
            }
            source.sendFailure(Component.literal("No creepers"));
            return 0;
        }
        source.sendFailure(Component.literal("Only players can run this command"));
        return 0;
    }
}
