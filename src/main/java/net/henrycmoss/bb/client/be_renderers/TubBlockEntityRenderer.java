package net.henrycmoss.bb.client.be_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import net.henrycmoss.bb.block.entity.TubBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class TubBlockEntityRenderer implements BlockEntityRenderer<TubBlockEntity> {

    private final ItemRenderer itemRenderer;

    public TubBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Grab the global ItemRenderer from the context
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(TubBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        // --- 1. CALCULATE FLUID HEIGHT & RENDER FLUID ---
        FluidStack fluidStack = entity.getFluidTank().getFluid();
        float fluidY = 0.25f; // Default bottom height when empty (4/16)

        if (!fluidStack.isEmpty()) {
            float minHeight = 0.25f;
            float maxHeight = 0.9375f; // (15/16)
            float fillPercentage = (float) fluidStack.getAmount() / entity.getFluidTank().getCapacity();
            fluidY = maxHeight;

            // [Insert the Fluid Rendering Code from the previous response here]
            renderFluid(poseStack, bufferSource, fluidStack, fluidY, packedLight, packedOverlay);
        }

        // --- 2. RENDER THE SOLID ITEM ---
        // Fetch the item from your BlockEntity's custom ItemStackHandler
        ItemStack solidItem = entity.getRenderedItem();

        if (!solidItem.isEmpty()) {
            poseStack.pushPose();

            // A. Position the item in the center of the cauldron
            // We set Y slightly above the fluid surface (fluidY + 0.05) so it looks like it's floating/resting on it

            float itemY = fluidStack.isEmpty() ? 0.3f : fluidY + 0.05f;
            poseStack.translate(0.5f, itemY, 0.5f);

            // B. Scale the item down so it fits nicely inside the cauldron walls
            poseStack.scale(0.4f, 0.4f, 0.4f);

            // C. Compatible with both item and block item
            /*BakedModel itemModel = this.itemRenderer.getModel(solidItem, entity.getLevel(), null, 0);
            if (itemModel.isGui3d()) {
                // It's a 3D block (e.g., Cobblestone). Let's let it sit flat, or rotate it slowly for a cool effect:
                long time = System.currentTimeMillis();
                float rotation = (time / 40f) % 360;
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            } else {
                // It's a flat 2D item (e.g., Apple, Wheat).
                // Lay it flat horizontally on the fluid surface instead of standing upright!
                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                // Add a subtle rotation so items don't all align perfectly North-South
                poseStack.mulPose(Axis.ZP.rotationDegrees(45f));
            }

            // D. Render the item using the Minecraft ItemRenderer
            this.itemRenderer.render(
                    solidItem,
                    ItemDisplayContext.GROUND, // Tells MC to treat it like an item dropped on the ground
                    false,                     // leftHanded
                    poseStack,
                    bufferSource,
                    packedLight,               // Standard block lighting
                    packedOverlay,             // Standard overlay texture (usually 10)
                    itemModel                  // The baked model we queried above
            );*/

            //Test for just items
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

            // Optional: Make it spin slowly over time!
            float gameTime = entity.getLevel() != null ? entity.getLevel().getGameTime() + partialTick : 0;
            poseStack.mulPose(Axis.ZP.rotationDegrees(gameTime * 2.0f)); // Spins on the horizontal plane

            // 4. Render the physical 3D item model!
            this.itemRenderer.renderStatic(
                    solidItem,
                    ItemDisplayContext.FIXED, // Fits perfectly flat inside blocks
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    entity.getLevel(),
                    (int) entity.getBlockPos().asLong()
            );

            poseStack.popPose();
        }
    }

    private void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource,
                             FluidStack fluidStack, float fluidY, int packedLight, int packedOverlay) {
        if(fluidStack.getFluid() == null) return;
        IClientFluidTypeExtensions clientExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        var stillTexture = clientExtensions.getStillTexture(fluidStack);

        //Prevent null passing into apply()
        if (stillTexture == null) {
            // Fallback directly to vanilla water still texture
            stillTexture = new ResourceLocation("minecraft", "block/water_still");
        }
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTexture);

        int tintColor = clientExtensions.getTintColor(fluidStack);
        float red = (float) (tintColor >> 16 & 255) / 255.0F;
        float green = (float) (tintColor >> 8 & 255) / 255.0F;
        float blue = (float) (tintColor & 255) / 255.0F;
        float alpha = (float) (tintColor >> 24 & 255) / 255.0F;

        // 3. Render the horizontal fluid plane inside the cauldron walls
        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();

        float xMin = 0.125f; // Inside edge (2/16)
        float xMax = 0.875f; // Inside edge (14/16)
        float zMin = 0.125f;
        float zMax = 0.875f;

        float uMin = sprite.getU(2);
        float uMax = sprite.getU(14);
        float vMin = sprite.getV(2);
        float vMax = sprite.getV(14);

        // Draw top face quad
        builder.vertex(matrix, xMin, fluidY, zMin).color(red, green, blue, alpha).uv(uMin, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
        builder.vertex(matrix, xMin, fluidY, zMax).color(red, green, blue, alpha).uv(uMin, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
        builder.vertex(matrix, xMax, fluidY, zMax).color(red, green, blue, alpha).uv(uMax, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
        builder.vertex(matrix, xMax, fluidY, zMin).color(red, green, blue, alpha).uv(uMax, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
    }
}
