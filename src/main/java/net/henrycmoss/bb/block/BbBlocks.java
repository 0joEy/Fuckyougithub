package net.henrycmoss.bb.block;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.custom.*;
import net.henrycmoss.bb.block.custom.fluid.BbFluids;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BbBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Bb.MODID);


    public static final RegistryObject<Block> COCAINE_TRAY = registerBlock("cocaine_tray",
            () -> new CocaineTrayBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(4f).requiresCorrectToolForDrops().noCollission()));

    public static final RegistryObject<Block> EPHEDRA_CROP = registerBlock("ephedra_crop",
            () -> new EphedraCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static final RegistryObject<Block> ETHER = registerBlock("ether",
            () -> new EtherBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)));

    public static final RegistryObject<LiquidBlock> ACID = registerBlock("acid",
            () -> new LiquidBlock(BbFluids.SOURCE_ACID, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> GEM_EMPOWERING_STATION = registerBlock("gem_empowering_station",
            () -> new GemEmpoweringStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> CRUCIBLE = registerBlock("crucible",
            () -> new CrucibleBlock(BlockBehaviour.Properties.copy(
                    Blocks.COBBLESTONE).noCollission()));

    public static final RegistryObject<Block> ELECTROLYTIC_CELL = registerBlock("electrolytic_cell",
            () -> new ElectrolyticCellBlock(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                            .noOcclusion()));

    public static final RegistryObject<Block> TEST = registerBlock("test",
            () -> new TestBlock(BlockBehaviour.Properties.copy(
                    Blocks.COBBLESTONE).noCollission()));


    public static final RegistryObject<Block> SULFUR_ORE = registerBlock("sulfur_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)));

    public static final RegistryObject<Block> MARIJUANA_BUSH = registerBlock("marijuana_bush",
            () -> new MarijuanaBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS)
                            .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> ERGOT_INFESTED_WHEAT_CROP = registerBlock("ergot_infested_wheat_crop",
            () -> new ErgotInfestedWheatCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission()
                    .randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> SHROOM_PATCH = registerBlock("shroom_patch",
            () -> new ShroomPatchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission()
                    .randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return BbItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
