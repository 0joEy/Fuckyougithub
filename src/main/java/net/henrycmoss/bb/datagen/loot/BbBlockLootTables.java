package net.henrycmoss.bb.datagen.loot;

import com.mojang.blaze3d.shaders.Uniform;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.EphedraCropBlock;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class BbBlockLootTables extends BlockLootSubProvider {

    public BbBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BbBlocks.ETHER.get());
        this.dropSelf(BbBlocks.COCAINE_TRAY.get());
        this.dropSelf(BbBlocks.GEM_EMPOWERING_STATION.get());
        this.dropSelf(BbBlocks.CRUCIBLE.get());
        this.add(BbBlocks.EPHEDRA_CROP.get(), createCropDrops(BbBlocks.EPHEDRA_CROP.get(), BbItems.EPHEDRA_STEM.get(),
                BbItems.EPHEDRA_SEEDS.get(), LootItemBlockStatePropertyCondition.hasBlockStateProperties(
                        BbBlocks.EPHEDRA_CROP.get()).setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(EphedraCropBlock.AGE, 3))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BbBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
