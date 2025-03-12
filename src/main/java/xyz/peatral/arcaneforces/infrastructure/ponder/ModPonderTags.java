package xyz.peatral.arcaneforces.infrastructure.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModItems;

public class ModPonderTags {

    public static final ResourceLocation
            WAYSTONES = Main.asResource("waystones"),
            ARCANE_MAGIC = Main.asResource("arcane_magic");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                RegisteredObjectsHelper::getKeyOrThrow);

        helper.registerTag(WAYSTONES)
                .addToIndex()
                .item(ModBlocks.WAYSTONE.asItem(), true, false)
                .title("Waystones")
                .description("Components that allow traversal across large distances")
                .register();

        helper.registerTag(ARCANE_MAGIC)
                .addToIndex()
                .item(ModItems.RITUAL_DAGGER.get(), true, false)
                .title("Arcane Magic")
                .description("Components that allow you to perform arcane magic")
                .register();

        HELPER.addToTag(WAYSTONES)
                .add(ModBlocks.WAYSTONE);

        itemHelper.addToTag(ARCANE_MAGIC)
                .add(ModItems.RITUAL_DAGGER.get());
    }
}
