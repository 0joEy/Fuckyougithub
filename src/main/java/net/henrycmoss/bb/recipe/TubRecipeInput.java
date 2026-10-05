package net.henrycmoss.bb.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TubRecipeInput extends SimpleContainer {
    private final FluidStack fluid;

    public TubRecipeInput(ItemStack item, FluidStack fluid) {
        super(item);
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }
}
