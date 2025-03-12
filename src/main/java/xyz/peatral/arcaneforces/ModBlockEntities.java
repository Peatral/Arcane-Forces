package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.arcaneforces.content.shrines.WaystoneBlockEntity;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<WaystoneBlockEntity> WAYSTONE = REGISTRATE.get().blockEntity("waystone", WaystoneBlockEntity::new).validBlock(ModBlocks.WAYSTONE).register();

    public static void register() {
    }
}
