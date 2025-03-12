package xyz.peatral.arcaneforces.infrastructure.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.infrastructure.ponder.scenes.WaystoneScenes;

public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(ModBlocks.WAYSTONE, "waystone/basic_usage", WaystoneScenes::basicUsage, ModPonderTags.WAYSTONES);

    }
}
