package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.arcaneforces.content.shrines.ShrineAltarBlockEntity;
import xyz.peatral.arcaneforces.content.shrines.WaystoneBlockEntity;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<WaystoneBlockEntity> WAYSTONE = REGISTRATE.get().blockEntity("waystone", WaystoneBlockEntity::new).validBlocks(ModBlocks.WAYSTONE).register();
    public static final BlockEntityEntry<ShrineAltarBlockEntity> SHRINE_ALTAR = REGISTRATE.get().blockEntity("shrine_altar", ShrineAltarBlockEntity::new).validBlocks(ModBlocks.SHRINE_ALTAR).register();

    public static void register() {
    }
}
