package net.henrycmoss.bb.events;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.tools.ShootingTools;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class CreeperListener {

    private static int tnts = 30;

    private static int yns = 0;

    private static boolean killing = false;


    @SubscribeEvent
    public static void creeperExplode(ExplosionEvent.Start event) {
        if (event.getExplosion().getExploder() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getExplosion().getExploder();
            Level level = creeper.level();
            float pX = 0;
            float pY = 0;
            for (int i = 0; i < 15; i++) {
                PrimedTnt tnt = new PrimedTnt(EntityType.TNT, level);
                tnt.setFuse(50);
                tnt.setPos(creeper.position());
                tnt.setDeltaMovement(ShootingTools.shootFromRotation(pX, pY,
                        0f, 2f));
                tnt.addTag("creeper");
                if(pX >= 360f) pX -= 360f;
                if (pY >= 360f) pY -= 360f;

                int m;

                if (pY <= 180f) {
                    m = (int) ((180 - pY) / 20);
                } else {
                    m = (int) ((pY - 180) / 20);
                }

                pY += 30 + m;
                pX += 75;
                LogUtils.getLogger().info(Integer.toString((int) pY));

                level.addFreshEntity(tnt);
            }
        } else if (event.getExplosion().getExploder() instanceof PrimedTnt tnt) {
            AABB area = AABB.ofSize(tnt.position(), 10000, 150, 10000);
            LogUtils.getLogger().info(tnts + ", " + killing);
            if(tnts < 5000 && !killing) {
                if (tnt.getTags().contains("creeper")) {
                    Random rand = new Random();
                    Level level = tnt.level();
                    float pX = 0;
                    float pY = 160;
                    for (int i = 0; i < 3; i++) {
                        PrimedTnt rTnt = new PrimedTnt(EntityType.TNT, level);

                        rTnt.setFuse(rand.nextInt(20, 150));
                        rTnt.setPos(tnt.position());
                        rTnt.setDeltaMovement(ShootingTools.shootFromRotation((pX), (pY),
                                0f, 2f));
                        rTnt.addTag("creeper");
                        if (pX >= 360f) pX -= 360f;
                        if (pY >= 360f) pY -= 360f;
                        pX += 125;
                        pY += 5;

                        level.addFreshEntity(rTnt);
                        tnts++;
                    }
                    LogUtils.getLogger().info("news");
                }
            }
            else {
                LogUtils.getLogger().info("else");
                tnt.level().getEntitiesOfClass(PrimedTnt.class, area, (primed -> primed.getTags().contains("creeper"))).forEach((primed) -> {
                    primed.kill();
                    tnts--;
                });
                killing = true;
                LogUtils.getLogger().info("Slimed " + (5000 - tnts) + " yns");
            }
            LogUtils.getLogger().info(killing + ", " + tnt.level().getEntitiesOfClass(PrimedTnt.class, area, (p -> p.getTags().contains("creeper"))).size());
            if (killing && (tnt.level().getEntitiesOfClass(PrimedTnt.class, area, (p) -> p.getTags().contains("creeper")).isEmpty())) {
                killing = false;
                tnts = 0;
                LogUtils.getLogger().info("resetting");
            }
        }
    }
}
