package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.item.Authorizations;
import net.minecraft.world.item.Item;

public class KeyCardItem extends Item {

    private Authorizations level;

    public KeyCardItem(Properties properties, Authorizations level) {
        super(properties);
        this.level = level;
    }

    public Authorizations getLevel() {
        return level;
    }
}
