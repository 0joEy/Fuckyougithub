package net.henrycmoss.bb.events.listeners;

import net.henrycmoss.bb.Bb;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bb.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LittererListener {

    @SubscribeEvent
    public static void dirtyItem(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        stack.getOrCreateTag().putBoolean("litter", true);
        stack.getOrCreateTag().putUUID("littererUUID", event.getPlayer().getUUID());
        event.getPlayer().getPersistentData().putBoolean("litterer", true);
        event.getPlayer().getPersistentData().putUUID("litterUUID", event.getEntity().getUUID());
    }
}
