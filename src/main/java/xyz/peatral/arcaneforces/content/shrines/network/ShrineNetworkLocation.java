package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ShrineNetworkLocation(String title, BlockPos position, boolean fastTravelable) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ShrineNetworkLocation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ShrineNetworkLocation::title,
            BlockPos.STREAM_CODEC, ShrineNetworkLocation::position,
            ByteBufCodecs.BOOL, ShrineNetworkLocation::fastTravelable,
            ShrineNetworkLocation::new
    );

    public static final Codec<ShrineNetworkLocation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("title").forGetter(ShrineNetworkLocation::title),
                    BlockPos.CODEC.fieldOf("position").forGetter(ShrineNetworkLocation::position),
                    Codec.BOOL.fieldOf("fastTravelable").forGetter(ShrineNetworkLocation::fastTravelable)
            ).apply(instance, ShrineNetworkLocation::new)
    );
}
