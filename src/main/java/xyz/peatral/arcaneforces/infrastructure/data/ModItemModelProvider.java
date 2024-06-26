package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.peatral.arcaneforces.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.RITUAL_DAGGER.get())
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(modid, "hastarget"), 1)
                .model(basicItem(ResourceLocation.fromNamespaceAndPath(modid, "ritualdagger_bloody")))
                .end();
        basicItem(ModItems.CURSED_RING.get());
        basicItem(ModItems.VOODOO_DOLL.get());
        basicItem(ModItems.FRAGRANT_RESIN.get());
    }
}
