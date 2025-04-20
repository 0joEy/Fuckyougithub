package net.henrycmoss.bb.datagen;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.item.BbItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BbItemModelProvider extends ItemModelProvider {

    public BbItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bb.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(BbItems.CHLORINE_GAS);
    }

    private ItemModelBuilder basicItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation(
                "item/generated")).texture("layer0", new ResourceLocation(Bb.MODID, "item/" + item.getId().getPath()));
    }


}
