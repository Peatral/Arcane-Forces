package xyz.peatral.arcaneforces;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.incense.TappingRecipe;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(
            BuiltInRegistries.RECIPE_TYPE,
            Main.MOD_ID
    );
    public static final DeferredHolder<RecipeType<?>, RecipeType<TappingRecipe>> TAPPING = register("tapping");

    public static final void register(IEventBus modEventBus) {
        RECIPES.register(modEventBus);
        Serializers.register(modEventBus);
    }

    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String pIdentifier) {
        return RECIPES.register(pIdentifier, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return pIdentifier;
            }
        });
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
                BuiltInRegistries.RECIPE_SERIALIZER,
                Main.MOD_ID
        );
        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TappingRecipe>> TAPPING = SERIALIZERS.register("tapping", () -> new RecipeSerializer<>() {
            @Override
            public MapCodec<TappingRecipe> codec() {
                return TappingRecipe.CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, TappingRecipe> streamCodec() {
                return TappingRecipe.STREAM_CODEC;
            }
        });

        public static final void register(IEventBus modEventBus) {
            SERIALIZERS.register(modEventBus);
        }
    }
}
