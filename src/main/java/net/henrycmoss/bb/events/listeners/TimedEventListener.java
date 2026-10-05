package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.util.TimedAction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TimedEventListener {

    static int time = 0;
    static int max = 45;

    public static boolean run = true;

    private static final List<TimedAction<Player, Level>> actions = List.of(
            ((player, level) -> {
                float pitch = 40;
                for(int p = 0; p < pitch; p += 2) {
                    for (int t = 0; t < 360; t += 6) {
                        Bee bee = EntityType.BEE.create(level);
                        Vec3 pos = pointAtTheta(t, 0, p, 3, player.position());
                        BlockState targetedBlock = level.getBlockState(BlockPos.containing(pos));
                        if(targetedBlock.getBlock() instanceof LiquidBlock || targetedBlock.is(Blocks.AIR)) {
                            bee.setPos(pos);
                            bee.setSpeed(10f);
                            if(!player.getAbilities().instabuild) bee.setTarget(player);
                            level.addFreshEntity(bee);
                        }
                    }
                }
            })
    );

    private static Vec3 pointAtTheta(float theta, float yaw, float pitch, float r, Vec3 center) {
        Vec3 n = new Vec3(Math.sin(pitch) * Math.cos(yaw),
                Math.sin(pitch) * Math.sin(yaw), Math.cos(pitch));
        Vec3 u = n.cross(n.add(1, 1, 1));
        Vec3 v = n.cross(u);
        return center.add(u.scale(r * Math.cos(theta))).add(v.scale(r * Math.sin(theta)));
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (run) {
            Level level = event.player.level();
            if ((++time) / 20 >= max) {
                max = level.getRandom().nextInt(30, 120);
                time = 0;
                actions.get(level.getRandom().nextInt(0, actions.size())).accept(event.player, level);
            }
        }
    }
}
