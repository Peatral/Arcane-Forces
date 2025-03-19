package xyz.peatral.arcaneforces.content.shrines.shrinealtar.holo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkLocation;

import java.util.Optional;

public class ShrineHoloRenderer extends EntityRenderer<ShrineHoloEntity> implements RenderLayerParent<ShrineHoloEntity, ShrineHoloModel<ShrineHoloEntity>> {

    private final ShrineHoloModel<ShrineHoloEntity> model;

    public ShrineHoloRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShrineHoloModel<>(context.bakeLayer(ShrineHoloModel.LAYER_LOCATION));
    }

    @Override
    public void render(ShrineHoloEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Optional<ShrineNetworkLocation> location = entity.getTarget();
        if (location.isEmpty()) {
            return;
        }
        float progress = entity.getAnimationLerp(partialTicks);
        Vec3 offset = entity.getEntityData().get(ShrineHoloEntity.PARENT).above().getCenter()
                .subtract(entity.getPosition(partialTicks))
                .scale(1 - progress);
        poseStack.pushPose();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.scale(progress, progress, progress);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        if (progress >= 1.0) {
            renderFloatingText(poseStack, bufferSource, Component.literal(location.get().title()), 0.0f, 0.6f, 0.0f, 0xFFFFFF, packedLight);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ShrineHoloModel<ShrineHoloEntity> getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureLocation(ShrineHoloEntity entity) {
        return Main.asResource("textures/entity/shrine/shrine_holo.png");
    }

    public void renderFloatingText(PoseStack poseStack, MultiBufferSource bufferSource, Component text, float x, float y, float z, int color, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
        poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.toRadians(camera.getYRot()), (float) Math.toRadians(-camera.getXRot()), 0.0F));
        Matrix4f matrix4f = poseStack.last().pose();
        int backgroundColor = (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.76F) * 255) << 24;
        Font font = Minecraft.getInstance().font;
        float xOffset = -font.width(text) / 2f;
        font.drawInBatch(text, xOffset, 0, color, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, backgroundColor, packedLight);
        poseStack.popPose();
    }
}
