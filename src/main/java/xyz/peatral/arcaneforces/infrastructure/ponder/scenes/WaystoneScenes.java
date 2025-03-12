package xyz.peatral.arcaneforces.infrastructure.ponder.scenes;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class WaystoneScenes {
    public static void basicUsage(SceneBuilder scene, SceneBuildingUtil util) {
        BlockPos waystoneA = util.grid().at(7, 1, 4);
        BlockPos waystoneB = util.grid().at(1, 1, 4);

        scene.title("basic_usage", "Using Waystones");
        scene.configureBasePlate(0, 0, 9);
        scene.showBasePlate();

        scene.idle(5);

        scene.world().showSection(util.select().position(waystoneA), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(15)
                .placeNearTarget()
                .text("Waystones can make up a network")
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(waystoneA));
        scene.idle(25);

        scene.world().showSection(util.select().position(waystoneB), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(20)
                .placeNearTarget()
                .text("Waystones placed within range connect to each other")
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(waystoneB));
        scene.idle(10);
        scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(waystoneA), util.vector().centerOf(waystoneB), 20);

        scene.idle(25);
        scene.markAsFinished();
    }
}
