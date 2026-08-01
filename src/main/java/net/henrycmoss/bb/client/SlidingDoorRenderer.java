package net.henrycmoss.bb.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.henrycmoss.bb.block.entity.SlidingDoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SlidingDoorRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {
    private BlockRenderDispatcher blockRenderer;

    public SlidingDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public void render(SlidingDoorBlockEntity be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight,
                       int packedOverlay) {
        Level level = be.getLevel();
        if(level != null) {
            BlockPos pos = be.getDoorPos();
            BlockState state = be.getBlockState();
            if(!state.isAir()) {
                ModelBlockRenderer.enableCaching();
                pose.pushPose();
                pose.translate(be.getXOffset(partialTicks), be.getYOffset(partialTicks), be.getZOffset(partialTicks));
            }

            pose.popPose();
            ModelBlockRenderer.clearCache();
        }
    }
}
