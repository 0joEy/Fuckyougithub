package net.henrycmoss.bb.entity.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.layers.BbModelLayers;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;

public class IRSAgentRenderer extends HumanoidMobRenderer<PathfinderMob, IRSAgentModel<PathfinderMob>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Bb.MODID, "textures/entity/irs_agent.png");

    public IRSAgentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, createModel(pContext.getModelSet(), ModelLayers.PIGLIN),  0.5F,
                1.0019531F, 1.0F, 1.0019531F);
    }

    private static IRSAgentModel<PathfinderMob> createModel(EntityModelSet pModelSet, ModelLayerLocation pLayer) {
        return new IRSAgentModel<>(pModelSet.bakeLayer(pLayer), false);
    }

    @Override
    public ResourceLocation getTextureLocation(PathfinderMob pEntity) {
        return TEXTURE;
    }
}
