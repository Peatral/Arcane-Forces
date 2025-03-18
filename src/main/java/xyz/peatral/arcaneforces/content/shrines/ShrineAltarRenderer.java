package xyz.peatral.arcaneforces.content.shrines;

import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;
import xyz.peatral.arcaneforces.ModPartialModels;
import xyz.peatral.arcaneforces.content.shrines.network.ClientShrineSavedData;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetwork;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkLocation;

import java.util.Optional;
import java.util.Set;

public class ShrineAltarRenderer implements BlockEntityRenderer<ShrineAltarBlockEntity> {
    public ShrineAltarRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public boolean shouldRenderOffScreen(ShrineAltarBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(ShrineAltarBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Optional<ShrineNetwork> optNetwork = ClientShrineSavedData.getNetwork(blockEntity.getLevel());
        if (optNetwork.isEmpty()) {
            return;
        }
        ShrineNetwork network = optNetwork.get();
        BlockPos pos = blockEntity.getBlockPos();
        Set<Triple<BlockPos, Integer, ShrineNetworkLocation>> locations = network.getShrines(pos);
        SuperByteBuffer shrineRender = CachedBuffers.partial(ModPartialModels.SHRINE, blockState);
        Optional<Double> furthestDistance = locations.stream().map(t -> t.getLeft().distSqr(pos)).max(Double::compareTo);
        if (furthestDistance.isEmpty()) {
            return;
        }
        locations.forEach(t -> {
            Vec3 shrinePos = t.getLeft().getCenter().subtract(pos.getCenter()).scale(2 / Math.sqrt(furthestDistance.get()));
            shrineRender
                    .translate(0, 1, 0)
                    .translate(shrinePos)
                    .light(packedLight)
                    .renderInto(poseStack, bufferSource.getBuffer(RenderType.SOLID));
        });
    }
}
