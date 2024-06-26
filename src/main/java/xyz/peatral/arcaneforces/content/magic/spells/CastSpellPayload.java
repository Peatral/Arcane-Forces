package xyz.peatral.arcaneforces.content.magic.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xyz.peatral.arcaneforces.Main;

public record CastSpellPayload(Holder<Spell> spell, ResolvableProfile target) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CastSpellPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "cast_spell"));

    public static final Codec<CastSpellPayload> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            Spell.CODEC.fieldOf("spell").forGetter(CastSpellPayload::spell),
                            ResolvableProfile.CODEC.fieldOf("target").forGetter(CastSpellPayload::target)
                    ).apply(instance, CastSpellPayload::new)
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, CastSpellPayload> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC,
            CastSpellPayload::spell,
            ResolvableProfile.STREAM_CODEC,
            CastSpellPayload::target,
            CastSpellPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePacketServer(final CastSpellPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> data.target().id().ifPresent(uuid -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null)
                return;
            Player target = server.getPlayerList().getPlayer(uuid);
            if (target == null || target.isRemoved() || !target.isAlive())
                return;
            data.spell().value().castCurse(context.player().level(), target, context.player());
        })).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MOD_ID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}
