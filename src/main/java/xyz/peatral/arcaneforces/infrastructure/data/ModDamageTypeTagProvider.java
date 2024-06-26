package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.arcaneforces.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagProvider extends DamageTypeTagsProvider {

    public ModDamageTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.DEATH_CURSE);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.DEATH_CURSE);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageTypes.DEATH_CURSE);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(ModDamageTypes.DEATH_CURSE);
        this.tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).add(ModDamageTypes.DEATH_CURSE);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.DEATH_CURSE);
    }
}
