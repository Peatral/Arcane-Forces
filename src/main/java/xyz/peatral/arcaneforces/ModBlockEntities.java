package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.arcaneforces.content.shrines.BellRingerBlockEntity;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<BellRingerBlockEntity> BELL_RINGER = REGISTRATE.get().blockEntity("bell_ringer", BellRingerBlockEntity::new).validBlock(ModBlocks.BELL_RINGER).register();

    public static void register() {
    }
}
