package net.henrycmoss.bb.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExistingLiquidItem extends Item implements BucketPickup {

    private final BucketItem bucket;

    public static final Map<Fluid, ExistingLiquidItem> fluidMap = new HashMap<>();

    public ExistingLiquidItem(Properties pProperties, BucketItem bucket) {
        super(pProperties);
        this.bucket = bucket;
        fluidMap.put(bucket.getFluid(), this);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(bucket);
    }

    public Fluid getFluid() {
        return this.bucket.getFluid();
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
