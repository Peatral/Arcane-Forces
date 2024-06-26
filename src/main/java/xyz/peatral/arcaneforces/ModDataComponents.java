package xyz.peatral.arcaneforces;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModDataComponents {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Main.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> AFFINITY = DATA_COMPONENTS.register("affinity",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());
    /*
    public static final Supplier<DataComponentType<UUID>> OWNER = DATA_COMPONENTS.register("owner",
            () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<UUID>> TARGET = DATA_COMPONENTS.register("target",
            () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).cacheEncoding().build());
     */

    public static final Supplier<DataComponentType<ResolvableProfile>> OWNER = DATA_COMPONENTS.register("owner",
            () -> DataComponentType.<ResolvableProfile>builder().persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<ResolvableProfile>> TARGET = DATA_COMPONENTS.register("target",
            () -> DataComponentType.<ResolvableProfile>builder().persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding().build());

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }

    @SubscribeEvent
    private static void modifyComponents(ModifyDefaultComponentsEvent event) {
        // Sets the component on melon seeds
        //event.modifyMatching(item -> item instanceof PickaxeItem, builder ->
        //        builder.set(RANKING.value(), RankingComponent.getDefault(null))
        //);
    }
}
