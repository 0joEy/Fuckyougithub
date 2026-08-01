package net.henrycmoss.bb.item.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum Authorizations {
    LEVEL_0(0, ItemStack.EMPTY),
    LEVEL_1(1, Items.COPPER_INGOT.getDefaultInstance()),
    LEVEL_2(1, Items.GOLD_INGOT.getDefaultInstance()),
    LEVEL_3(1, Items.DIAMOND.getDefaultInstance()),
    LEVEL_4(1, Items.CONDUIT.getDefaultInstance()),
    LEVEL_5(1, Items.NETHER_BRICKS.getDefaultInstance());

    private final int id;
    private final ItemStack stack;

    Authorizations(int id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }

    public int getLevel() {
        return id;
    }

    public ItemStack getItem() {
        return stack;
    }

    public static Authorizations fromId(int id) {
        for(Authorizations a : Authorizations.values()) {
            if(a.id == id) return a;
        }
        return null;
    }

    public static Authorizations fromItem(ItemStack stack) {
        for(Authorizations a : Authorizations.values()) {
            if(a.stack == stack) return a;
        }
        return null;
    }
}
