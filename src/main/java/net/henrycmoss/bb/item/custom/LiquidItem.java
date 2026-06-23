package net.henrycmoss.bb.item.custom;

import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Map;
import java.util.Optional;

public class LiquidItem extends Item implements BucketPickup {

    private final CustomBucketItem bucket;

    /*public static final Map<Fluid, LiquidItem> fluidMap = Map.of(Fluids.WATER.getSource(), (LiquidItem) BbItems.WATER.get(),
            Fluids.LAVA, (LiquidItem) BbItems.LAVA.get());*/

    public LiquidItem(Properties properties, CustomBucketItem bucket) {
        super(properties);
        this.bucket = bucket;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(bucket);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }


}
