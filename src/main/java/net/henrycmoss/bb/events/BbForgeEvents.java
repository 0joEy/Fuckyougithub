package net.henrycmoss.bb.events;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.command.*;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.tools.ShootingTools;
import net.henrycmoss.bb.villager.BbVillagers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BbForgeEvents {

    private static boolean jumped = false;

    private static Projectile lastPro = null;
    private static PrimedTnt lastTnt = null;
    private static List<PrimedTnt> originals = new ArrayList<>();

    private static int tnts = 30;

    private static boolean killing = false;

    @SubscribeEvent
    public static void registerCommandsEvent(RegisterCommandsEvent event) {
        new HomeCommand(event.getDispatcher());
        new FlyCommand(event.getDispatcher());
        new IgniteCommand(event.getDispatcher());
        new GearCommand(event.getDispatcher());
        new TreasureCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }





    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if (!event.getOriginal().level().isClientSide()) {
            event.getEntity().getPersistentData().putIntArray(Bb.MODID + ":homepos", event.getOriginal()
                    .getPersistentData().getIntArray(Bb.MODID + ":homepos"));
        }
    }

    @SubscribeEvent
    public static void addBbTrades(VillagerTradesEvent event) {
        if (event.getType() == BbVillagers.METH_COOK.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(BbItems.MARIJUANA.get(), 1), 100, 8, 0.1f
            ));
        }
    }


    /* @SubscribeEvent
    public static void meteorTouchGround(TickEvent.PlayerTickEvent event) {
        List<FallingBlockEntity> entities = event.player.level().getEntitiesOfClass(FallingBlockEntity.class,
                AABB.ofSize(event.player.position(), 50, 50, 50));
        for (FallingBlockEntity entity : entities) {
            event.player.sendSystemMessage(Component.literal(entity.serializeNBT().get("BlockState").toString()));
            if (entity.serializeNBT().get("BlockState").equals("{Name:'minecraft:redstone_block'}")) {
                event.player.sendSystemMessage(Component.literal(entity.serializeNBT().get("Time").toString()));
                if (entity.serializeNBT().get("OnGround").equals(true)) {
                    event.player.sendSystemMessage(Component.literal("fkfkfkfk"));
                }
            }
        }
    } */



    /* @SubscribeEvent
    public static void skeletonShoot(EntityEvent event) {
        if (event.getEntity() instanceof Skeleton skeleton) {
            if (skeleton.bowGoal != null) skeleton.bowGoal.attackRadiusSqr = 30 * 30;
            if (skeleton.getAttributes() != null && skeleton.getAttribute(Attributes.FOLLOW_RANGE) != null) {
                skeleton.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Carrots", 50d, AttributeModifier.Operation.ADDITION));
            }
        }
        else if (event.getEntity() instanceof Projectile pro) {
            if (pro != lastPro) {
                if (pro.isAlive() && pro.getOwner() instanceof Skeleton skeleton) {
                    lastPro = pro;
                    Level level = pro.level();
                    PrimedTnt tnt = new PrimedTnt(EntityType.TNT, level);
                    tnt.setPos(pro.position());
                    tnt.setDeltaMovement(pro.getDeltaMovement());
                    tnt.setFuse(80);
                    tnt.addTag("tntdynamite");
                    pro.kill();
                    pro.remove(Entity.RemovalReason.KILLED);
                    pro.checkDespawn();
                    level.addFreshEntity(tnt);

                    LogUtils.getLogger().info("pro");
                }
            }
        }
        List<PrimedTnt> tnts = event.getEntity().level().getEntitiesOfClass(PrimedTnt.class, new AABB(event.getEntity().getX() - 100, event.getEntity().getY() - 100, event.getEntity().getZ() - 100, event.getEntity().getX() + 100, event.getEntity().getY() + 100,
                event.getEntity().getZ() + 100), (tnt) -> tnt.getTags().contains("tntdynamite"));

        if (!tnts.isEmpty()) {
            for (PrimedTnt tnt : tnts) {
                if (tnt != lastTnt) {
                    Vec3 pos = tnt.position();
                    AABB area = new AABB(pos.x() - 1, pos.y() - 1, pos.z() - 1, pos.x() + 1, pos.y() + 1, pos.z() + 1);
                    if (false) {
                        tnt.setFuse(5);
                        lastTnt = tnt;
                        originals.add(tnt);
                    } else if (BlockPos.betweenClosedStream(area).anyMatch((blockPos -> event.getEntity().level().getBlockState(blockPos).getBlock() != Blocks.AIR)) || tnt.onGround()) {
                        tnt.setFuse(5);
                        lastTnt = tnt;
                        originals.add(tnt);
                    }
                    LogUtils.getLogger().info(Integer.toString(tnt.getFuse()));
                } else if (lastTnt != null) {
                    if (!lastTnt.isAlive()) { originals.remove(lastTnt); lastTnt = null; }
                }
            }
        }
    } /*

    @SubscribeEvent
    public static void tntCannon(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        List<PrimedTnt> tnts = level.getEntitiesOfClass(PrimedTnt.class,
                new AABB(player.getX() - 100, player.getY() - 100, player.getZ() - 100,
                        player.getX() + 100, player.getY() + 100, player.getZ() + 100));

        Optional<PrimedTnt> pTnts = tnts.stream().filter((pTnt) -> pTnt.getFuse() < 10 && pTnt.getTags().contains("cannon")).findFirst();

        if(pTnts.isPresent()) {
            float pX = 0;
            float pY = 0;
            for(int i = 0; i < 5; i++) {
                PrimedTnt tnt = new PrimedTnt(EntityType.TNT, level);
                tnt.setFuse(10);
                tnt.setPos(pTnts.get().position());
            tnt.setDeltaMovement(ShootingTools.shootFromRotation((player.getXRot() + pX), (player.getYRot() + pY),
                    0f, 1.5f));
            pX += 10;
            pY += 10;
            level.addFreshEntity(tnt);
            }
        }
    }

    @SubscribeEvent
    public static void angerEntityEvent(LivingChangeTargetEvent event) {
        if ((event.getNewTarget() instanceof ServerPlayer) && event.getNewTarget().hasEffect(BbEffects.HALLUCINATION.get())) {
            HallucinationEffect.setHaunter(event.getEntity());
            LogUtils.getLogger().info("log: " + event.getOriginalTarget());
        } else {
            event.getEntity().setSpeed(0.1f);
            HallucinationEffect.setHaunter(null);
        }
    }*/



    /*@SubscribeEvent
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
    }*/
    /*

    @SubscribeEvent
    public static void entityJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player) {
            jumped = true;
        }
    } */

}
