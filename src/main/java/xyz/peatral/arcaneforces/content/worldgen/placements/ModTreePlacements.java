package xyz.peatral.arcaneforces.content.worldgen.placements;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.content.worldgen.features.ModTreeFeatures;

public class ModTreePlacements {
    public static final ResourceKey<PlacedFeature> OLIBANUM_CHECKED = key("olibanum_checked");
    public static final ResourceKey<PlacedFeature> MYRRH_CHECKED = key("myrrh_checked");
    public static final ResourceKey<PlacedFeature> OLIBANUM = key("olibanum");
    public static final ResourceKey<PlacedFeature> MYRRH = key("myrrh");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> olibanumHolder = holdergetter.getOrThrow(ModTreeFeatures.OLIBANUM);
        Holder<ConfiguredFeature<?, ?>> myrrhHolder = holdergetter.getOrThrow(ModTreeFeatures.MYRRH);

        PlacementUtils.register(context, OLIBANUM_CHECKED, olibanumHolder, PlacementUtils.filteredByBlockSurvival(ModBlocks.OLIBANUM_SAPLING.get()));
        PlacementUtils.register(context, MYRRH_CHECKED, myrrhHolder, PlacementUtils.filteredByBlockSurvival(ModBlocks.MYRRH_SAPLING.get()));
        PlacementUtils.register(context, OLIBANUM, olibanumHolder);
        PlacementUtils.register(context, MYRRH, myrrhHolder);
    }

    private static ResourceKey<PlacedFeature> key(String pKey) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, pKey));
    }
}
