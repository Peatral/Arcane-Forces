package xyz.peatral.arcaneforces.content.incense;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.level.BlockEvent;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.ModRecipeTypes;
import xyz.peatral.arcaneforces.Utils;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public record TappingRecipe(
        BlockState input,
        List<String> requiredProps,
        BlockState block,
        List<String> propsToCopy,
        ChanceDrop drop,
        boolean requiresBlood
) implements Recipe<SingleRecipeInput> {
    public static final MapCodec<TappingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("input_state").forGetter(TappingRecipe::input),
                    Codec.list(Codec.STRING).fieldOf("required_properties").forGetter(TappingRecipe::requiredProps),
                    BlockState.CODEC.fieldOf("replaced_state").forGetter(TappingRecipe::block),
                    Codec.list(Codec.STRING).fieldOf("properties_to_copy").forGetter(TappingRecipe::propsToCopy),
                    ChanceDrop.CODEC.fieldOf("drop").forGetter(TappingRecipe::drop),
                    Codec.BOOL.fieldOf("requires_blood").forGetter(TappingRecipe::requiresBlood)
            ).apply(instance, TappingRecipe::new)
    );
    public static final ItemAbility TAPPING = ItemAbility.get("tapping");
    // TODO: replace this with something better
    private static final StreamCodec<ByteBuf, BlockState> BLOCKSTATE_STREAM_CODEC = ByteBufCodecs.fromCodec(BlockState.CODEC);
    private static final StreamCodec<ByteBuf, List<String>> STRING_LIST_STREAM_CODEC = ByteBufCodecs.fromCodec(Codec.list(Codec.STRING));

    public static final StreamCodec<RegistryFriendlyByteBuf, TappingRecipe> STREAM_CODEC = StreamCodec.composite(
            BLOCKSTATE_STREAM_CODEC,
            TappingRecipe::input,
            STRING_LIST_STREAM_CODEC,
            TappingRecipe::requiredProps,
            BLOCKSTATE_STREAM_CODEC,
            TappingRecipe::block,
            STRING_LIST_STREAM_CODEC,
            TappingRecipe::propsToCopy,
            ChanceDrop.STREAM_CODEC,
            TappingRecipe::drop,
            ByteBufCodecs.BOOL,
            TappingRecipe::requiresBlood,
            TappingRecipe::new
    );

    @SubscribeEvent
    public static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility().equals(TAPPING)) {
            BlockState inputState = event.getState();
            ItemStack stack = event.getHeldItemStack();
            RecipeManager recipeManager = event.getPlayer().level().getRecipeManager();
            Optional<RecipeHolder<TappingRecipe>> foundRecipe = recipeManager
                    .getAllRecipesFor(ModRecipeTypes.TAPPING.get())
                    .stream()
                    .filter(r -> {
                        TappingRecipe hr = r.value();
                        return hr.testBlock(inputState) && (!hr.requiresBlood() || stack.has(ModDataComponents.TARGET));
                    }).findFirst();

            if (foundRecipe.isEmpty())
                return;

            TappingRecipe recipe = foundRecipe.get().value();
            if (!event.isSimulated()) {
                if (!event.getLevel().isClientSide()) {
                    ItemStack drop = recipe.drop.roll();
                    if (!drop.isEmpty()) {
                        if (!event.getPlayer().addItem(drop.copy())) {
                            event.getLevel().addFreshEntity(new ItemEntity(event.getPlayer().level(), event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ(), drop.copy()));
                        }
                    }
                    if (recipe.requiresBlood() && !event.getPlayer().isCreative()) {
                        stack.remove(ModDataComponents.TARGET);
                    }
                }
                event.setFinalState(recipe.transformBlock(inputState));
            }
        }
    }

    public boolean testBlock(BlockState in) {
        if (!input.getBlock().equals(in.getBlock()))
            return false;
        if (requiredProps.isEmpty())
            return true;
        return input.getProperties()
                .stream()
                .filter(property -> requiredProps.contains(property.getName()))
                .allMatch(property -> input.getValue(property).equals(in.getValue(property)));
    }

    public BlockState transformBlock(BlockState in) {
        BlockState out = block;
        for (Property<?> prop : in.getProperties()) {
            if (propsToCopy.contains(prop.getName())) {
                out = Utils.copyProperty(prop, in, out);
            }
        }
        return out;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider) {
        ItemStack output = drop.stack().copy();
        return output.isEmpty() ? new ItemStack(block.getBlock()) : output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        ItemStack output = drop.stack().copy();
        return output.isEmpty() ? new ItemStack(block.getBlock()) : output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.Serializers.TAPPING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.TAPPING.get();
    }
}
