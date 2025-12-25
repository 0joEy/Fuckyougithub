package net.henrycmoss.bb.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.henrycmoss.bb.Bb;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TestScreen extends AbstractContainerScreen<TestMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Bb.MODID,
            "textures/gui/test.png");

    public TestScreen(TestMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        gui.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderArrow(gui, x, y);
    }

    public void renderArrow(GuiGraphics gui, int x, int y) {
        if(menu.isCrafting()) {
            gui.blit(TEXTURE, /* progress arrow x */ x + 79,
                    /*progress arrow y*/ y + 34, /* x pos ref arrow */ 176,
                    /* ref y start*/ 14, /* ref x offset end */ menu.getScaledProgress(),
                    /*ref y offset end */ 17);
        }
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        renderBackground(gui);

        super.render(gui, mouseX, mouseY, partialTick);
        renderTooltip(gui, mouseX, mouseY);
    }
}
