package net.henrycmoss.bb.entity.client;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.entity.custom.PoliceOfficerEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;

public class PoliceOfficerRenderer extends HumanoidMobRenderer<PathfinderMob, PoliceOfficerModel<PathfinderMob>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Bb.MODID, "textures/entity/police_officer.png");

    public PoliceOfficerRenderer(EntityRendererProvider.Context context) {
        super(context, createModel(context.getModelSet(), ModelLayers.PLAYER),  0.5F,
                1.0019531F, 1.0F, 1.0019531F);
    }

    private static PoliceOfficerModel<PathfinderMob> createModel(EntityModelSet pModelSet, ModelLayerLocation pLayer) {
        return new PoliceOfficerModel<>(pModelSet.bakeLayer(pLayer), false);
    }

    @Override
    public ResourceLocation getTextureLocation(PathfinderMob pEntity) {
        return TEXTURE;
    }
}
