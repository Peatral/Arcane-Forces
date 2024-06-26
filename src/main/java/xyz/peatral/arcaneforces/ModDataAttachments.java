package xyz.peatral.arcaneforces;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;

import java.util.function.Supplier;

public class ModDataAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Main.MOD_ID);

    public static final Supplier<AttachmentType<SpellSelector>> SPELL_SELECTOR = ATTACHMENT_TYPES.register(
            "spell_selector", () -> AttachmentType
                    .builder(() -> new SpellSelector(0, 3, 0, 0))
                    .serialize(SpellSelector.CODEC)
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
