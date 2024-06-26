package xyz.peatral.arcaneforces.content.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModDataAttachments;

public record SpellSelector(int slot, int slotAmount, int cooldown, int cooldownMax) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SpellSelector> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "curse_selector"));

    public static final Codec<SpellSelector> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("slot").forGetter(SpellSelector::slot),
                    Codec.INT.fieldOf("slot_amount").forGetter(SpellSelector::slotAmount),
                    Codec.INT.fieldOf("cooldown").forGetter(SpellSelector::cooldown),
                    Codec.INT.fieldOf("cooldown_max").forGetter(SpellSelector::cooldownMax)
            ).apply(instance, SpellSelector::new)
    );
    public static final StreamCodec<ByteBuf, SpellSelector> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SpellSelector::slot,
            ByteBufCodecs.VAR_INT,
            SpellSelector::slotAmount,
            ByteBufCodecs.VAR_INT,
            SpellSelector::cooldown,
            ByteBufCodecs.VAR_INT,
            SpellSelector::cooldownMax,
            SpellSelector::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePacketClient(final SpellSelector data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    context.player().setData(ModDataAttachments.SPELL_SELECTOR, data);
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable(Main.MOD_ID + ".networking.failed", e.getMessage()));
                    return null;
                });
    }

    public static void handlePacketServer(final SpellSelector data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().setData(ModDataAttachments.SPELL_SELECTOR, data);
        }).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MOD_ID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}