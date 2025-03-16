package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ShrineNetworkLocation(String title) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ShrineNetworkLocation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ShrineNetworkLocation::title,
            ShrineNetworkLocation::new
    );

    public static final Codec<ShrineNetworkLocation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("title").forGetter(ShrineNetworkLocation::title)
            ).apply(instance, ShrineNetworkLocation::new)
    );
}
