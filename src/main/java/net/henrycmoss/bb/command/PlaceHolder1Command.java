package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PlaceHolder1Command {

    public PlaceHolder1Command(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("ph1").executes(
                (source) -> test(source.getSource(), source.getSource().getPlayer())));
    }

    public int test(CommandSourceStack source, Player player) {
        ServerLevel level = source.getLevel();
        Creeper creeper = EntityType.CREEPER.create(level);
        creeper.moveTo(player.position().add(player.getLookAngle()));
        creeper.addTag("lava");
        level.addFreshEntity(creeper);
        return 1;
    }
}
