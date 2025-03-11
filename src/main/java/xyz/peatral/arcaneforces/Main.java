package xyz.peatral.arcaneforces;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import xyz.peatral.arcaneforces.infrastructure.data.ModLanguageProvider;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "arcaneforces";

    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MOD_ID));

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        ModCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register();
        ModBlockEntities.register();
        ModMobEffects.register();
        ModSpells.register(modEventBus);
        ModEnchantmentEffects.register();
        ModDataComponents.register(modEventBus);
        ModDataAttachments.register();
        ModRecipeTypes.register(modEventBus);
        ModLanguageProvider.register();
    }
}
