package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.util.BbTags;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum ElectrolysisResultType {

    SOLID(0, BbTags.Items.SOLIDS, BbTags.Blocks.SOLIDS),
    LIQUID(1, BbTags.Items.LIQUIDS, BbTags.Blocks.LIQUIDS),
    GAS(2, BbTags.Items.GASSES, BbTags.Blocks.GASSES);

    private final int id;
    private final TagKey<Item> iTag;
    private final TagKey<Block> bTag;

     ElectrolysisResultType(int id, TagKey<Item> iTag, TagKey<Block> bTag) {
        this.id = id;
        this.iTag = iTag;
        this.bTag = bTag;
    }

    public int getId() {
        return id;
    }

    public ElectrolysisResultType fromId(int id) {
        for(ElectrolysisResultType type : ElectrolysisResultType.values()) {
            if(type.getId() == id) return type;
        }
        return null;
    }

    public static ElectrolysisResultType fromName(String name) {
        return ElectrolysisResultType.valueOf(name.toUpperCase());
    }

    public TagKey<Item> getItemTag() {
        return iTag;
    }

    public TagKey<Block> getBlockTag() {
        return bTag;
    }


}
