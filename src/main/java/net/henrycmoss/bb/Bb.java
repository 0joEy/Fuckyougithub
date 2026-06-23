package net.henrycmoss.bb;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.fluid.BbFluidTypes;
import net.henrycmoss.bb.block.custom.fluid.BbFluids;
import net.henrycmoss.bb.block.entity.BbBlockEntities;
import net.henrycmoss.bb.client.HallucinationRenderer;
import net.henrycmoss.bb.client.ShroomsRenderer;
import net.henrycmoss.bb.effect.BbEffects;
import net.henrycmoss.bb.entity.BbEntities;
import net.henrycmoss.bb.entity.client.EvilPigRenderer;
import net.henrycmoss.bb.entity.client.IRSAgentRenderer;
import net.henrycmoss.bb.entity.client.MissileRenderer;
import net.henrycmoss.bb.entity.client.PoliceOfficerRenderer;
import net.henrycmoss.bb.entity.custom.EvilPigEntity;
import net.henrycmoss.bb.entity.custom.IRSAgentEntity;
import net.henrycmoss.bb.entity.custom.PoliceOfficerEntity;
import net.henrycmoss.bb.item.BbItems;
import net.henrycmoss.bb.network.BbNetwork;
import net.henrycmoss.bb.recipe.BbRecipeTypes;
import net.henrycmoss.bb.recipe.BbRecipes;
import net.henrycmoss.bb.screen.*;
import net.henrycmoss.bb.tab.BbCreativeModeTabs;
import net.henrycmoss.bb.villager.BbVillagers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Bb.MODID)
public class Bb {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bb";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final HallucinationRenderer TEST_RENDERER = new HallucinationRenderer();

    public static final ShroomsRenderer SHROOMS_RENDERER = new ShroomsRenderer();

    public Bb() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerAttributes);
        //modEventBus.addListener(EventPriority.LOW, Bb::gatherData);

        BbNetwork.register();

        BbItems.register(modEventBus);
        BbBlocks.register(modEventBus);
        BbCreativeModeTabs.register(modEventBus);
        BbVillagers.register(modEventBus);
        BbEffects.register(modEventBus);
        BbFluids.register(modEventBus);
        BbFluidTypes.register(modEventBus);
        BbBlockEntities.register(modEventBus);
        LogUtils.getLogger().info("menus registered");
        BbMenuTypes.register(modEventBus);
        BbRecipes.register(modEventBus);
        BbRecipeTypes.register(modEventBus);
        BbEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        //BbPlacementModifiers.register(modEventBus);
        //BbFeatures.register(modEventBus);
        //BbPlacementModifiers.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BbCreativeModeTabs.BB.getKey()) {
            event.accept(BbItems.METHAMPHETAMINE);
            event.accept(BbItems.MARIJUANA);
            event.accept(BbItems.UNLIT_JOINT);
            event.accept(BbItems.JOINT);
            event.accept(BbItems.EPHEDRA_SEEDS);
            event.accept(BbItems.EPHEDRA_STEM);
            event.accept(BbItems.EPHEDRINE);
            event.accept(BbItems.PLASTIC_BAG);
            event.accept(BbItems.SULFURIC_ACID_BUCKET);
            event.accept(BbItems.TNT_CANNON);
            event.accept(BbItems.ALCOHOL_BOTTLE);
            event.accept(BbBlocks.COCAINE_TRAY);
            event.accept(BbBlocks.ETHER);
            event.accept(BbBlocks.CRUCIBLE);
            event.accept(BbItems.PSEUDOEPHEDRINE);
        }
    }

    @Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(BbBlocks.EPHEDRA_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BbBlocks.ERGOT_INFESTED_WHEAT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BbBlocks.MARIJUANA_BUSH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BbFluids.SOURCE_ACID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(BbFluids.FLOWING_ACID.get(), RenderType.translucent());

            EntityRenderers.register(BbEntities.EVIL_PIG.get(), EvilPigRenderer::new);
            EntityRenderers.register(BbEntities.IRS_AGENT.get(), IRSAgentRenderer::new);
            EntityRenderers.register(BbEntities.POLICE_OFFICER.get(), PoliceOfficerRenderer::new);
            EntityRenderers.register(BbEntities.MISSILE.get(), MissileRenderer::new);

            MinecraftForge.EVENT_BUS.register(Bb.TEST_RENDERER);
            MinecraftForge.EVENT_BUS.register(Bb.SHROOMS_RENDERER);

            MenuScreens.register(BbMenuTypes.GEM_EMPOWERING_MENU.get(), GemEmpoweringStationScreen::new);
            MenuScreens.register(BbMenuTypes.CRUCIBLE_MENU.get(), CrucibleScreen::new);
            MenuScreens.register(BbMenuTypes.ELECTROLYTIC_CELL.get(), ElectrolyticCellScreen::new);
            MenuScreens.register(BbMenuTypes.TEST_MENU.get(), TestScreen::new);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BbEntities.EVIL_PIG.get(), EvilPigEntity.createAttributes().build());
        event.put(BbEntities.IRS_AGENT.get(), IRSAgentEntity.createAttributes().build());
        event.put(BbEntities.POLICE_OFFICER.get(), PoliceOfficerEntity.createAttributes().build());
    }
}
