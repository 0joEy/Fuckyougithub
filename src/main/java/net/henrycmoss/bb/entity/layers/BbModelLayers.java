package net.henrycmoss.bb.entity.layers;

import net.henrycmoss.bb.Bb;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class BbModelLayers {

    public static final ModelLayerLocation PIG_LAYER =
            new ModelLayerLocation(new ResourceLocation(Bb.MODID, "pig_layer"), "pig_layer");
    public static final ModelLayerLocation AGENT_LAYER =
            new ModelLayerLocation(new ResourceLocation(Bb.MODID, "agent_layer"), "agent_layer");
    public static final ModelLayerLocation OFFICER_LAYER =
            new ModelLayerLocation(new ResourceLocation(Bb.MODID, "officer_layer"), "officer_layer");
    public static final ModelLayerLocation MISSILE_LAYER =
            new ModelLayerLocation(new ResourceLocation(Bb.MODID, "missile_layer"), "missile_layer");
}
