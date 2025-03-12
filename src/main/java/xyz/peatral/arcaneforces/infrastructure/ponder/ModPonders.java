package xyz.peatral.arcaneforces.infrastructure.ponder;

import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import xyz.peatral.arcaneforces.Main;

public class ModPonders implements PonderPlugin {
    @Override
    public String getModId() {
        return Main.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ModPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        ModPonderTags.register(helper);
    }
}
