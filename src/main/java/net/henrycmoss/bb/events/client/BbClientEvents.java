package net.henrycmoss.bb.events.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.entity.BbBlockEntities;
import net.henrycmoss.bb.block.entity.TubBlockEntity;
import net.henrycmoss.bb.client.be_renderers.TubBlockEntityRenderer;
import net.henrycmoss.bb.input.KeyMappings;
import net.henrycmoss.bb.network.BbNetwork;
import net.henrycmoss.bb.network.packet.ForcePushPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BbClientEvents {

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyMappings.R_KEY.get());
    }

    /*@SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if(KeyMappings.R_KEY.get().consumeClick()) {
                BbNetwork.CHANNEL.sendToServer(new ForcePushPacket());
            }
        }

    }*/

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BbBlockEntities.TUB_BE.get(), TubBlockEntityRenderer::new);
    }
}
