package xyz.peatral.arcaneforces.core.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.client.gui.CursedRingGuiLayer;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ClientModEvents {
    public static final Lazy<KeyMapping> CAST_SPELL_MAPPING = Lazy.of(() -> new KeyMapping("key." + Main.MOD_ID + ".cast_spell", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories." + Main.MOD_ID));

    @SubscribeEvent
    private static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(CAST_SPELL_MAPPING.get());
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "spell_selection"),
                new CursedRingGuiLayer()
        );
    }
}
