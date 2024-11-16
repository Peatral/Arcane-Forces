package xyz.peatral.arcaneforces;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "arcaneforces";

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMobEffects.register(modEventBus);
        ModSpells.register(modEventBus);
        ModEnchantmentEffects.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModDataAttachments.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
    }
}
