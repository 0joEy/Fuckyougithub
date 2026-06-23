package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class SubjectsCommand {

    private static final List<EntityType<?>> types = List.of(
            EntityType.PIG,
            EntityType.COW,
            EntityType.SHEEP,
            EntityType.RABBIT,
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.ENDERMAN,
            EntityType.WITHER_SKELETON,
            EntityType.BLAZE,
            EntityType.HUSK
    );

    public SubjectsCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("subjects").then(Commands.argument("pos",
                Vec3Argument.vec3()).executes(source ->
                    summonSubjects(source.getSource(), Vec3Argument.getVec3(source, "pos"),
                            new Random().nextInt(0, 31)))
                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(source -> summonSubjects(source.getSource(),
                                Vec3Argument.getVec3(source, "pos"),
                                IntegerArgumentType.getInteger(source, "amount"))))));
    }

    private int summonSubjects(CommandSourceStack source, Vec3 pos, int amount) {
        Random rand = new Random();
        for(int i = 0; i < amount; i++) {
            Level level = source.getLevel();
            Entity entity = types.get(rand.nextInt(0, types.size())).create(level);
            entity.moveTo(pos);
            level.addFreshEntity(entity);
        }
        return 1;
    }
}
