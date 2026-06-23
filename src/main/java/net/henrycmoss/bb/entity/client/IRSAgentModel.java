package net.henrycmoss.bb.entity.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;

public class IRSAgentModel<T extends PathfinderMob> extends PlayerModel<T> {

    public IRSAgentModel(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }

    public static LayerDefinition createLayers() {
        MeshDefinition meshDefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition partdefinition = meshDefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16)
                .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, CubeDeformation.NONE), PartPose.ZERO);
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, CubeDeformation.NONE).texOffs(31, 1)
                .addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE).texOffs(2, 4)
                .addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 32);
    }


}
