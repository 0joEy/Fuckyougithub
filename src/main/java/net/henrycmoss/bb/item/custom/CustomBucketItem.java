package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.item.BbItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class CustomBucketItem extends Item {

    private LiquidItem fluid = null;

    public CustomBucketItem(Properties pProperties) {
        super(pProperties);
    }

    public LiquidItem getFluid() {
        return fluid;
    }

    public LiquidItem setFluid() {
        fluid = new LiquidItem(new Item.Properties().stacksTo(16), this);
        return fluid;
    }
}
