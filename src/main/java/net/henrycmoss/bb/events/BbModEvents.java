package net.henrycmoss.bb.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.villager.BbVillagers;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BbModEvents {


        /*private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
                .add(Registries.BIOME, BbBiomesRegister::bootStrapBiomes);

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent event)
        {
            DataGenerator generator = event.getGenerator();
            PackOutput output = generator.getPackOutput();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), BUILDER,
                    Set.of(Bb.MODID)));
        }

*/


}


        //private static final CustomBossEvent bar = new CustomBossEvent(new ResourceLocation("bsbar", Bb.MODID), Component.literal("test boss"));

        /*@SubscribeEvent
        public static void entityTickEvent(LivingEvent.LivingTickEvent event) {
            if (event.getEntity().getType() == EntityType.SLIME && event.getEntity().getTags().contains("testBoss")) {
                bar.setVisible(true);
                bar.setValue((int) event.getEntity().getHealth());
                bar.setColor(BossEvent.BossBarColor.RED);
                for (Player p : event.getEntity().level().players()) {
                    bar.addPlayer((ServerPlayer) p);
                }
            }*/

