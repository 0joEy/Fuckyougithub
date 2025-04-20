package net.henrycmoss.bb.datagen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BbBlockStateProvider extends BlockStateProvider {

    public BbBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Bb.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BbBlocks.ETHER);
        blockWithItem(BbBlocks.SULFUR_ORE);
    }

    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
