package net.henrycmoss.bb.item;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.custom.fluid.BbFluids;
import net.henrycmoss.bb.food.BbFoods;
import net.henrycmoss.bb.item.custom.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            Bb.MODID);


    public static final RegistryObject<Item> JOINT = ITEMS.register("blunt",
            () -> new JointItem(new Item.Properties().food(BbFoods.JOINT)));

    public static final RegistryObject<Item> UNLIT_JOINT = ITEMS.register("unlit_blunt",
            () -> new UnlitJointItem(new Item.Properties()));

    public static final RegistryObject<Item> MARIJUANA = ITEMS.register("marijuana",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PLASTIC_BAG = ITEMS.register("plastic_bag",
            () -> new PlasticBagItem(new Item.Properties()));

    public static final RegistryObject<Item> EPHEDRA_SEEDS = ITEMS.register("ephedra_seeds",
            () -> new ItemNameBlockItem(BbBlocks.EPHEDRA_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> EPHEDRA_STEM = ITEMS.register("ephedra_stem",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 500, 0),
                            1f).build())));

    public static final RegistryObject<Item> EPHEDRINE = ITEMS.register("ephedrine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PSEUDOEPHEDRINE = ITEMS.register("pseudoephedrine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METHAMPHETAMINE = ITEMS.register("methamphetamine",
            () -> new Item(new Item.Properties().food(BbFoods.METHAMPHETAMINE)));

    public static final RegistryObject<Item> SULFURIC_ACID_BUCKET = ITEMS.register("sulfuric_acid_bucket",
            () -> new BucketItem(BbFluids.SOURCE_ACID, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> TNT_CANNON = ITEMS.register("tnt_cannon",
            () -> new TntCannonItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ALCOHOL_BOTTLE = ITEMS.register("alcohol_bottle",
            () -> new AlcoholBottleItem(new Item.Properties().stacksTo(1)
                    .craftRemainder(Items.GLASS_BOTTLE).food(BbFoods.ALCOHOL)));

    public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur",
            () -> new SulfurItem(new Item.Properties()));

    public static final RegistryObject<Item> TERMINAL = ITEMS.register("terminal",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTERY_FLUID = ITEMS.register("battery_fluid",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PLASTIC = ITEMS.register("plastic",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GRAPHITE = ITEMS.register("graphite",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LSD_TABLET = ITEMS.register("lsd_tablet",
            () -> new Item(new Item.Properties().food(BbFoods.LSD_TABLET)));

    public static final RegistryObject<Item> ERGOT_INFESTED_WHEAT_SEEDS = ITEMS.register("ergot_infested_wheat_seeds",
            () -> new ItemNameBlockItem(BbBlocks.ERGOT_INFESTED_WHEAT_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> ERGOT_INFESTED_WHEAT = ITEMS.register("ergot_infested_wheat",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ERGOT = ITEMS.register("ergot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAGIC_MUSHROOMS = ITEMS.register("magic_mushrooms",
            () -> new Item(new Item.Properties().food(BbFoods.MAGIC_MUSHROOMS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
