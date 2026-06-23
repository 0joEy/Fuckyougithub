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
import org.checkerframework.checker.units.qual.C;

import java.util.function.Supplier;

public class BbItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            Bb.MODID);


    public static final RegistryObject<Item> JOINT = ITEMS.register("blunt",
            () -> new JointItem(new Item.Properties().food(BbFoods.JOINT)));

    public static final RegistryObject<UnlitJointItem> UNLIT_JOINT = ITEMS.register("unlit_blunt",
            () -> new UnlitJointItem(new Item.Properties()));

    public static final RegistryObject<Item> MARIJUANA = ITEMS.register("marijuana",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<PlasticBagItem> PLASTIC_BAG = ITEMS.register("plastic_bag",
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

    public static final RegistryObject<TntCannonItem> TNT_CANNON = ITEMS.register("tnt_cannon",
            () -> new TntCannonItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ALCOHOL_BOTTLE = ITEMS.register("alcohol_bottle",
            () -> new AlcoholBottleItem(new Item.Properties().stacksTo(1)
                    .craftRemainder(Items.GLASS_BOTTLE).food(BbFoods.ALCOHOL)));

    public static final RegistryObject<Item> SALT = ITEMS.register("salt",
            () -> new Item(new Item.Properties()));

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

    public static final RegistryObject<Item> ERGOT_INFESTED_WHEAT = ITEMS.register("ergot_infested_wheat",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ERGOT = ITEMS.register("ergot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HYDROGEN_GAS = ITEMS.register("hydrogen_gas",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CHLORINE_GAS = ITEMS.register("chlorine_gas",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HCL = ITEMS.register("hcl",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAGIC_MUSHROOMS = ITEMS.register("magic_mushrooms",
            () -> new Item(new Item.Properties().food(BbFoods.MAGIC_MUSHROOMS)));

    public static final RegistryObject<Item> COCA_LEAF = ITEMS.register("coca_leaf",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<MissileLauncherItem> MISSILE_LAUNCHER = ITEMS.register("missile_launcher",
            () -> new MissileLauncherItem(new Item.Properties()));

    public static final RegistryObject<CustomBucketItem> COCA_PASTE_BUCKET = addCustomBucket("coca_paste",
            new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));

    public static final RegistryObject<Item> COCAINE = ITEMS.register("cocaine",
            () -> new Item(new Item.Properties().food(BbFoods.METHAMPHETAMINE)));

    public static final RegistryObject<HotChickenItem> HOT_CHICKEN = ITEMS.register("daves_hot_chicken",
            () -> new HotChickenItem(new Item.Properties().food(BbFoods.DAVES_HOT_CHICKEN)));

    public static final RegistryObject<Item> FIRE_STICK = ITEMS.register("fire_stick",
            () -> new FireStickItem(new Item.Properties().stacksTo(1)));

    /*public static final RegistryObject<Item> WATER = addCustomBucket("water",
            () -> new LiquidItem(new Item.Properties(), Items.WATER_BUCKET));

    public static final RegistryObject<Item> LAVA = addCustomBucket("lava",
            () -> new LiquidItem(new Item.Properties(), Items.LAVA_BUCKET));

    public static final RegistryObject<Item> MILK = addLiquidItem("water",
            () -> new LiquidItem(new Item.Properties(), Items.MILK_BUCKET));*/

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<CustomBucketItem> addCustomBucket(String fluidName, Item.Properties properties) {
        RegistryObject<CustomBucketItem> bucket = ITEMS.register(fluidName + "_bucket",
                () -> new CustomBucketItem(properties));
        ITEMS.register(fluidName, () -> bucket.get().setFluid());
        return bucket;
    }
}
