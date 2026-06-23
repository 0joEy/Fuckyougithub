package net.henrycmoss.bb.recipe;

import com.mojang.logging.LogUtils;
import net.henrycmoss.bb.util.BbTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public enum ItemState {

    NONE(0, null, null),
    SOLID(1, BbTags.Blocks.SOLIDS, BbTags.Items.SOLIDS),
    LIQUID(2, BbTags.Blocks.LIQUIDS, BbTags.Items.LIQUIDS),
    GAS(3, BbTags.Blocks.GASSES, BbTags.Items.GASSES);

    private final int id;
    private final TagKey<Block> bTag;
    private final TagKey<Item> iTag;

     ItemState(int id, TagKey<Block> bTag, TagKey<Item> iTag) {
        this.id = id;
        this.bTag = bTag;
        this.iTag = iTag;
    }

    public int getId() {
        return id;
    }

    public static ItemState fromId(int id) {
        for(ItemState type : ItemState.values()) {
            if(type.getId() == id) return type;
        }
        return ItemState.NONE;
    }

    public static ItemState fromName(String name) {
        return ItemState.valueOf(name.toUpperCase());
    }

    public static ItemState get(Item item) {
         if(item instanceof BlockItem blockItem) {
             BlockState block = blockItem.getBlock().defaultBlockState();
             for(ItemState type : ItemState.values()) {
                 if(type != ItemState.NONE && block.is(type.getBlockTag())) return type;
             }
         }
         for(ItemState type : ItemState.values()) {
             if(type != ItemState.NONE && item.getDefaultInstance().is(type.getItemTag())) {
                 LogUtils.getLogger().info("type: " + type);
                 return type;
             }
         }
         ItemState type =  item == Items.AIR || item.getDefaultInstance() == ItemStack.EMPTY ?
                 ItemState.NONE : ItemState.SOLID;
         LogUtils.getLogger().info("Type last resort: " + type);
         return type;
    }

    public TagKey<Block> getBlockTag() {
        return bTag;
    }

    public TagKey<Item> getItemTag() {
         return iTag;
    }


}
