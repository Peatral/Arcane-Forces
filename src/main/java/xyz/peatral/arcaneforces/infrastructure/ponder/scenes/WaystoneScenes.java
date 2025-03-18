package xyz.peatral.arcaneforces.infrastructure.ponder.scenes;

import com.mojang.authlib.GameProfile;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.content.shrines.waystone.WaystoneBlock;

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
        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Activated Waystones placed within range connect to each other")
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(waystoneB));
        scene.idle(35);

        ResolvableProfile targetProfile = new ResolvableProfile(new GameProfile(UUIDUtil.createOfflinePlayerUUID("target"), "target"));
        ItemStack dagger = new ItemStack(ModItems.RITUAL_DAGGER.get());
        dagger.applyComponents(
                DataComponentMap.builder()
                        .set(ModDataComponents.TARGET.get(), targetProfile)
                        .build()
        );

        scene.overlay().showControls(util.vector().blockSurface(waystoneA, Direction.UP), Pointing.DOWN, 30)
                        .rightClick()
                        .withItem(dagger);
        scene.idle(10);
        scene.world().modifyBlock(waystoneA, state -> state.setValue(WaystoneBlock.ACTIVATED, true), false);
        scene.idle(25);

        scene.overlay().showControls(util.vector().blockSurface(waystoneB, Direction.UP), Pointing.DOWN, 30)
                .rightClick()
                .withItem(dagger);
        scene.idle(10);
        scene.world().modifyBlock(waystoneB, state -> state.setValue(WaystoneBlock.ACTIVATED, true), false);
        scene.idle(25);

        scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(waystoneA), util.vector().centerOf(waystoneB), 20);

        scene.idle(25);
        scene.markAsFinished();
    }
}
