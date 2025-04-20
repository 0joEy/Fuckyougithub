package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.henrycmoss.bb.Bb;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TreasureCommand {

    public TreasureCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("treasure").then(Commands.argument("times", IntegerArgumentType.integer())
                .executes((source) -> drop(source.getSource(), source.getSource().getPlayer(),
                        IntegerArgumentType.getInteger(source, "times"))))).createBuilder();
    }

    private int drop(CommandSourceStack source, Player player, int x) {
        Vec3 pos = player.position();
        ServerLevel server = source.getServer().getLevel(source.getLevel().dimension());

        if(server != null) {
            for(int i = 0; i < x; i++) {
                LootTable loot = source.getServer().getLootData().getLootTable(
                        new ResourceLocation(Bb.MODID, "gameplay/treasure"));

                List<ItemStack> items = loot.getRandomItems(new LootParams.Builder(server).withParameter(LootContextParams.ORIGIN, pos)
                        .create(LootContextParamSet.builder().build())).stream().toList();

                Random rand = new Random();

                items.forEach((stack) -> {
                    ItemEntity eItem = new ItemEntity(server, rangedOffset(pos.x(), 5, rand), pos.y() + 10,
                            rangedOffset(pos.z(), 5, rand), stack);
                    server.addFreshEntity(eItem);
                });
            }

            source.sendSuccess(() -> Component.literal("coming in hot"), false);
            return 1;
        }

        source.sendFailure(Component.literal("nah"));
        return 0;
    }


    private double rangedOffset(double val, double reach, Random rand) {
        return rand.nextDouble(val - reach, val + reach);
    }
}
