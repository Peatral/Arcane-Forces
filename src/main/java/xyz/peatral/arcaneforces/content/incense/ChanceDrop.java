package xyz.peatral.arcaneforces.content.incense;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public record ChanceDrop(ItemStack stack, float chance) {
    private static final Random random = new Random();

    public static final Codec<ChanceDrop> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("stack").forGetter(ChanceDrop::stack),
                    Codec.FLOAT.fieldOf("chance").forGetter(ChanceDrop::chance)
            ).apply(instance, ChanceDrop::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceDrop> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ChanceDrop::stack,
            ByteBufCodecs.FLOAT,
            ChanceDrop::chance,
            ChanceDrop::new
    );

    public ItemStack roll() {
        int outputAmount = stack.getCount();
        for (int roll = 0; roll < stack.getCount(); roll++)
            if (random.nextFloat() >= chance)
                outputAmount--;
        if (outputAmount == 0)
            return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }
}
