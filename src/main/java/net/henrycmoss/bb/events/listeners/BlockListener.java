package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.tools.LevelTools;
import net.henrycmoss.bb.util.BbTags;
import net.henrycmoss.bb.util.BlockEventAction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Bb.MODID)

public class BlockListener {


    private static final List<Block> blocks = new ArrayList<>(ForgeRegistries.BLOCKS.getValues());
    private static final List<BlockEventAction<Player, BlockPos, Level>> blockMap = List.of(
            (player, pos, level) -> {
                int rand = new Random().nextInt(0, 1004);
                Block block = blocks.get(rand);
                level.setBlock(pos, block.defaultBlockState(), 3);
                player.sendSystemMessage(Component.literal("block set son, id: " + rand + ", block: " +
                        block.defaultBlockState().getBlock().getDescriptionId()));
            },

            ((player, position, level) -> anvilDrop(player, level)),

            ((player, position, level) -> {
                PrimedTnt tnt = EntityType.TNT.create(level);
                tnt.setFuse(10);
                tnt.setPos(position.getCenter());
                for(int i = 0; i < 4; i++) level.addFreshEntity(tnt);
            }),

            (((player, position, level) -> {
                Random rand = new Random();
                int bound = rand.nextInt(0, 31);

                for(int i = 0; i < bound; i++) {
                    double yaw = rand.nextDouble(0, 360) * (2 * Math.PI / 180d);
                    double roll = rand.nextDouble(45, 90) * (2 * Math.PI / 180d);
                    Vec3 dir = new Vec3(Math.cos(yaw), Math.sin(yaw), Math.sin(roll));
                    ItemEntity item = EntityType.ITEM.create(level);
                    item.setItem(BbItems.HOT_CHICKEN.get().getDefaultInstance());
                    //Change to yaw and roll rotations?
                    item.moveTo(position, player.getYRot(), player.getXRot());
                    item.setDeltaMovement(dir.scale(2d));
                    level.addFreshEntity(item);
                }
            })),
            BlockListener::replaceTrees,
            BlockListener::summonBigBoss

    );

    @SubscribeEvent
    public static void randomBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        Random rand = new Random();
        if(rand.nextInt(0, 100) > 90) {
            int id = rand.nextInt(0, blockMap.size());
            player.sendSystemMessage(Component.literal(id + "!!"));
            blockMap.get(id).accept(player, pos, player.level());
        }
    }

    private static void anvilDrop(Player player, Level level) {
        int height = 20;
        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
        LevelTools.fillBlocks(level, pos, 1, height, 1, Blocks.AIR.defaultBlockState());
        level.setBlock(pos.offset(0, height, 0), Blocks.ANVIL.defaultBlockState(), 3);
        ThrownPotion potion = EntityType.POTION.create(level);
        potion.deserializeNBT(PotionUtils.setCustomEffects(Items.POTION.getDefaultInstance(),
                List.of(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 15))).getTag());
        potion.setPos(player.position().add(0, 5, 0));
        potion.setDeltaMovement(0, -0.3d, 0);
        level.addFreshEntity(potion);
    }

    private static void replaceTrees(Player player, BlockPos pos, Level level) {
        int base = 20;
        int height = 3;
        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-base, 0, -base),
                pos.offset(base, height, base))) {
            BlockState block = level.getBlockState(pos);
            if(block.is(BbTags.Blocks.LOGS) || block.is(BbTags.Blocks.LEAVES)) {
                level.removeBlock(pos, false);
            }
        }
    }

    private static void summonBigBoss(Player player, BlockPos pos, Level level) {
        Fireball fireball = EntityType.FIREBALL.create(level);
        fireball.addTag("bigboss");
        fireball.moveTo(pos.getX(), pos.getY() + 35, pos.getZ());
        fireball.yPower = -0.5d;
        level.addFreshEntity(fireball);
    }
}
