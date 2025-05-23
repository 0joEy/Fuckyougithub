package net.henrycmoss.bb.util;

import net.henrycmoss.bb.Bb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BbTags {

    public static class Blocks{

        public static TagKey<Block> tag(String location) {
            return BlockTags.create(new ResourceLocation(Bb.MODID, location));
        }
        public static TagKey<Block> forgeTag(String location) {
            return BlockTags.create(new ResourceLocation("forge", location));
        }

        public static final TagKey<Block> SOLIDS = forgeTag("solids");
        public static final TagKey<Block> LIQUIDS = forgeTag("liquids");
        public static final TagKey<Block> GASSES = forgeTag("gasses");
    }

    public static class Items{

        public static final TagKey<Item> FRUITS = tag("fruits");

        public static final TagKey<Item> SOLIDS = forgeTag("solids");
        public static final TagKey<Item> LIQUIDS = forgeTag("liquids");
        public static final TagKey<Item> GASSES = forgeTag("gasses");

        public static TagKey<Item> tag(String location) {
            return ItemTags.create(new ResourceLocation(Bb.MODID,  location));
        }
        public static TagKey<Item> forgeTag(String location) {
            return ItemTags.create(new ResourceLocation("forge", location));
        }
    }
}
