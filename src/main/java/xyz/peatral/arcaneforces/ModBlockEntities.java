package xyz.peatral.arcaneforces;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.peatral.arcaneforces.content.shrines.BellRingerBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Main.MOD_ID);

    public static final Supplier<BlockEntityType<BellRingerBlockEntity>> BELL_RINGER = BLOCK_ENTITY_TYPES.register(
            "bell_ringer",
            () -> BlockEntityType.Builder.of(
                            BellRingerBlockEntity::new,
                            ModBlocks.BELL_RINGER.block().get().defaultBlockState().getBlock()
                    )
                    .build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
