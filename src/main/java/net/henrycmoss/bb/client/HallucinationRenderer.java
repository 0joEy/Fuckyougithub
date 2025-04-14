package net.henrycmoss.bb.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.effect.BbEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HallucinationRenderer {

    public static final int MAX_DISTANCE = 10;
    public static final ResourceLocation TEST_SHADER = new ResourceLocation(Bb.MODID,
            "shaders/post/lsd.json");

    public static final ResourceLocation TEST_TEXTURE = new ResourceLocation(Bb.MODID,
            "textures/overlay/test.png");

    public boolean thirdEyeActiveLastTick = false;


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        tick(event);
    }

    public void renderOverlay(PoseStack pose) {
        RenderSystem.setShaderTexture(0, HallucinationRenderer.TEST_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        RenderSystem.clearColor(1, 1, 1, 1);

        Window window = Minecraft.getInstance().getWindow();
        pose.pushPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        Tesselator t = Tesselator.getInstance();
        BufferBuilder buf = t.getBuilder();

        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.vertex(0.0D, window.getGuiScaledHeight(), -90.0D).uv(0.0f, 1.0f).endVertex();
        buf.vertex(window.getGuiScaledWidth(), window.getGuiScaledHeight(), -90.0D)
                .uv(1.0f, 1.0f).endVertex();
        buf.vertex(window.getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
        buf.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
        t.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        pose.popPose();

        RenderSystem.clearColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.hasEffect(BbEffects.HALLUCINATION.get())) {
            if (event.player.getEffect(BbEffects.HALLUCINATION.get()).getDuration() > 1) {
                if (!thirdEyeActiveLastTick) {
                    thirdEyeActiveLastTick = true;

                    Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.loadEffect(TEST_SHADER));
                }
            }
        }
        else {
            if (this.thirdEyeActiveLastTick) {
                this.thirdEyeActiveLastTick = false;
                    //unload shader
                    Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
            }
        }
    }
}
