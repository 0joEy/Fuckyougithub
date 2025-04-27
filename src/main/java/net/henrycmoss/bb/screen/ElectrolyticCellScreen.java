package net.henrycmoss.bb.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.henrycmoss.bb.Bb;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectrolyticCellScreen extends AbstractContainerScreen<ElectrolyticCellMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Bb.MODID,
            "textures/gui/electrolytic_cell.png");

    public ElectrolyticCellScreen(ElectrolyticCellMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
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

        int anX = x + 58;
        int catX = x + 104;

        int pY = y + 22;

        renderAnode(gui, anX, pY);
        replace(gui, anX, pY, menu.getScaledProgress1());
        renderCathode(gui, catX, pY);
        replace(gui, catX, pY, menu.indicatorSize() - menu.getScaledProgress1());
    }

    private void renderAnode(GuiGraphics gui, int aX, int aY) {
        if(menu.isCrafting()) {
            gui.blit(TEXTURE, /* progress arrow x */ aX,
                    /*progress arrow y*/ aY, /* x pos ref arrow */ 180,
                    /* ref y start*/ 1, /* ref x offset end */ 16,
                    /*ref y offset end */ 35);
        }
    }

    private void renderCathode(GuiGraphics gui, int cX, int cY) {
        if(menu.isCrafting()) {
            gui.blit(TEXTURE, /* progress arrow x */  cX,
                    /*progress arrow y*/ cY, /* x pos ref arrow */ 198,
                    /* ref y start*/ 1, /* ref x offset end */ 16,
                    /*ref y offset end */ 35);
        }
    }

    private void renderArrow(GuiGraphics gui, int x, int y) {
        if(menu.isCrafting()) {
            gui.blit(TEXTURE, /* progress arrow x */ x + 77,
                    /*progress arrow y*/ y + 44, /* x pos ref arrow */ 179,
                    /* ref y start*/ 40, /* ref x offset end */ 24,
                    /*ref y offset end */ menu.getScaledProgress());
        }
    }

    private void replace(GuiGraphics gui, int pX, int pY, int progress) {
        if(menu.isCrafting()) {
            gui.blit(TEXTURE, pX, pY, 216, 1, 16, progress);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
