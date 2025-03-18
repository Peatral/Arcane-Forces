package xyz.peatral.arcaneforces;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo.ShrineHoloRenderer;
import xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo.ShrineHoloEntity;

import static xyz.peatral.arcaneforces.Main.REGISTRATE;

public class ModEntities {
    public static final EntityEntry<ShrineHoloEntity> SHRINE_HOLO = REGISTRATE.get().entity("shrine_holo", ShrineHoloEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.25f, 0.25f).eyeHeight(0.25f))
            .renderer(() -> ShrineHoloRenderer::new)
            .register();

    public static void register() {
    }
}
