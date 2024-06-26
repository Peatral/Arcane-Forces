package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModDamageTypes;
import xyz.peatral.arcaneforces.ModEnchantments;
import xyz.peatral.arcaneforces.ModTrees;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class DatagenHandler {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModTrees.Features::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        CompletableFuture<HolderLookup.Provider> modLookupProvider = DatagenHandler.modLookupProvider();

        generator.addProvider(event.includeClient(), new DatapackBuiltinEntriesProvider(output, modLookupProvider, BUILDER, Set.of(Main.MOD_ID)));

        ModBlockTagProvider modBlockTagProvider = generator.addProvider(event.includeClient(), new ModBlockTagProvider(output, modLookupProvider, Main.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemTagProvider(output, modLookupProvider, modBlockTagProvider.contentsGetter(), Main.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModEnchantmentTagProvider(output, modLookupProvider, Main.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModDamageTypeTagProvider(output, modLookupProvider, Main.MOD_ID, existingFileHelper));

        generator.addProvider(event.includeClient(), new ModLootTableProvider(output, lookupProvider));

        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, Main.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, Main.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, Main.MOD_ID, "en_us"));
        generator.addProvider(event.includeClient(), new ModRecipeProvider(output, lookupProvider));
    }

    public static CompletableFuture<HolderLookup.Provider> modLookupProvider() {
        return CompletableFuture.supplyAsync(() -> {
            RegistryAccess.Frozen registryaccess$frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
            HolderLookup.Provider holderlookup$provider = BUILDER.build(registryaccess$frozen);
            return holderlookup$provider;
        }, Util.backgroundExecutor());
    }
}
