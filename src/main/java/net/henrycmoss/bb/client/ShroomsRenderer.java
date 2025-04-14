package net.henrycmoss.bb.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.effect.BbEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShroomsRenderer {

    public static final ResourceLocation SHROOMS_SHADER = new ResourceLocation(Bb.MODID, "shaders/post/shrooms.json");

    public boolean effectActiveLastTick = false;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        tick(event);
    }

    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.hasEffect(BbEffects.SHROOMS.get())) {
            if (event.player.getEffect(BbEffects.SHROOMS.get()).getDuration() > 1) {
                if (!effectActiveLastTick) {
                    effectActiveLastTick = true;

                    Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer
                            .loadEffect(SHROOMS_SHADER));
                }
            }
        } else {
            if (this.effectActiveLastTick) {
                this.effectActiveLastTick = false;
                //unload shader
                Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
            }
        }
    }
}
