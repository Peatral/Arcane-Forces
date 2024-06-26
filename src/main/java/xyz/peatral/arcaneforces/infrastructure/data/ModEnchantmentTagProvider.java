package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModEnchantments;
import xyz.peatral.arcaneforces.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends EnchantmentTagsProvider {

    public ModEnchantmentTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.Enchantments.SPELLS)
                .add(
                        ModEnchantments.POISON_CURSE,
                        ModEnchantments.FIRE_CURSE,
                        ModEnchantments.DEATH_CURSE,
                        ModEnchantments.FLIGHT_BLESSING
                );

        tag(EnchantmentTags.CURSE)
                .add(
                        ModEnchantments.POISON_CURSE,
                        ModEnchantments.FIRE_CURSE,
                        ModEnchantments.DEATH_CURSE
                );
    }
}
