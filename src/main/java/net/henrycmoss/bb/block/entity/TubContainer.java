package net.henrycmoss.bb.block.entity;

import net.minecraft.world.SimpleContainer;
import net.minecraftforge.fluids.FluidStack;

public class TubContainer extends SimpleContainer {
    private FluidStack fluid;

    public TubContainer(int size, FluidStack fluid) {
        super(size);
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }
}
