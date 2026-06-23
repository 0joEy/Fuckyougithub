package net.henrycmoss.bb.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.custom.EvilPigEntity;
import net.henrycmoss.bb.entity.layers.BbModelLayers;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EvilPigRenderer extends MobRenderer<EvilPigEntity, EvilPigModel<EvilPigEntity>> {

    private static final ResourceLocation PIG_LOCATION =
            new ResourceLocation(Bb.MODID,"textures/entity/pig/pig.png");

    public EvilPigRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new EvilPigModel<>(pContext.bakeLayer(BbModelLayers.PIG_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EvilPigEntity pEntity) {
        return PIG_LOCATION;
    }

    @Override
    public void render(EvilPigEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.45f, 0.45f, 0.45f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
