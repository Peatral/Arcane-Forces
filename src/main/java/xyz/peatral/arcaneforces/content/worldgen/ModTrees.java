package xyz.peatral.arcaneforces.content.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;
import xyz.peatral.arcaneforces.content.worldgen.features.ModTreeFeatures;

import java.util.Optional;

public class ModTrees {
    public static final TreeGrower OLIBANUM = new TreeGrower(
            "olibanum",
            0.0f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(ModTreeFeatures.OLIBANUM),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static final TreeGrower MYRRH = new TreeGrower(
            "myrrh",
            0.0f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(ModTreeFeatures.MYRRH),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}
