package net.henrycmoss.bb.tab;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BbCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            Bb.MODID);

    public static final RegistryObject<CreativeModeTab> BB = TABS.register("bb_tab",
            () -> CreativeModeTab.builder().title(Component.literal("Breaking Bedrock")).
        icon(() -> new ItemStack(BbItems.METHAMPHETAMINE.get(), 1)).build());


    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
