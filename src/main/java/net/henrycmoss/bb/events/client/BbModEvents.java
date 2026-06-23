package net.henrycmoss.bb.events.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.command.*;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Bb.MODID)
public class BbModEvents {


    @SubscribeEvent
    public static void registerCommandsEvent(RegisterCommandsEvent event) {

        CommandBuildContext context = CommandBuildContext.simple(ClientRegistryLayer.createRegistryAccess().compositeAccess(), FeatureFlagSet.of(FeatureFlags.VANILLA));

        new HomeCommand(event.getDispatcher());
        new FlyCommand(event.getDispatcher());
        new IgniteCommand(event.getDispatcher());
        new GearCommand(event.getDispatcher(), context);
        new TreasureCommand(event.getDispatcher());
        new SubjectsCommand(event.getDispatcher());
        new PlaceHolder1Command(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }



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

