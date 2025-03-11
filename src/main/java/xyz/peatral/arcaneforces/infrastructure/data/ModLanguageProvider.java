package xyz.peatral.arcaneforces.infrastructure.data;

import xyz.peatral.arcaneforces.*;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModLanguageProvider {
    static {
        REGISTRATE.get().addRawLang(Main.MOD_ID + ".tooltip.rank", "Rank %1$d: %2$s/%3$s");
        REGISTRATE.get().addRawLang(Main.MOD_ID + ".tooltip.ownedby", "Owned by: %1$s");
        REGISTRATE.get().addRawLang(Main.MOD_ID + ".tooltip.target", "Target: %1$s");

        REGISTRATE.get().addRawLang(Main.MOD_ID + ".ranking.toast.title", "RANK UP!");
        REGISTRATE.get().addRawLang(Main.MOD_ID + ".ranking.toast.description", "%1$s [%2$s]");

        REGISTRATE.get().addRawLang("key.categories." + Main.MOD_ID, "Arcane Forces");
        REGISTRATE.get().addRawLang("key." + Main.MOD_ID + ".cast_spell", "Cast Invocation");

        REGISTRATE.get().addRawLang(Main.MOD_ID + ".networking.failed", "Error while networking!");
    }

    public static void register() {}
}
