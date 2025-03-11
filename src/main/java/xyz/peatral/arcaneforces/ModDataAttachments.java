package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModDataAttachments {
    public static final RegistryEntry<AttachmentType<?>, AttachmentType<SpellSelector>> SPELL_SELECTOR = REGISTRATE.get().generic(
            "spell_selector",
            NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            () -> AttachmentType
                    .builder(() -> new SpellSelector(0, 3, 0, 0))
                    .serialize(SpellSelector.CODEC)
                    .build()
    ).register();

    public static void register() {
    }
}
