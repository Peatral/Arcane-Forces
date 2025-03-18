package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.arcaneforces.content.shrines.ShrineAltarBlockEntity;
import xyz.peatral.arcaneforces.content.shrines.ShrineAltarRenderer;
import xyz.peatral.arcaneforces.content.shrines.WaystoneBlockEntity;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<WaystoneBlockEntity> WAYSTONE = REGISTRATE.get().blockEntity("waystone", WaystoneBlockEntity::new).validBlock(ModBlocks.WAYSTONE).register();
    public static final BlockEntityEntry<ShrineAltarBlockEntity> SHRINE_ALTAR = REGISTRATE.get().blockEntity("shrine_altar", ShrineAltarBlockEntity::new).renderer(() -> ShrineAltarRenderer::new).validBlock(ModBlocks.SHRINE_ALTAR).register();

    public static void register() {
    }
}
