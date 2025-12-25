package net.henrycmoss.bb.screen;

import net.henrycmoss.bb.Bb;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BbMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Bb.MODID);

    public static final RegistryObject<MenuType<GemEmpoweringStationMenu>> GEM_EMPOWERING_MENU =
            registerMenuType(GemEmpoweringStationMenu::new, "gem_empowering_menu");

    public static final RegistryObject<MenuType<CrucibleMenu>> CRUCIBLE_MENU =
            registerMenuType(CrucibleMenu::new, "crucible_menu");

    public static final RegistryObject<MenuType<ElectrolyticCellMenu>> ELECTROLYTIC_CELL =
            registerMenuType(ElectrolyticCellMenu::new, "electrolytic_cell");

    public static final RegistryObject<MenuType<TestMenu>> TEST_MENU =
            registerMenuType(TestMenu::new, "test_menu");


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}