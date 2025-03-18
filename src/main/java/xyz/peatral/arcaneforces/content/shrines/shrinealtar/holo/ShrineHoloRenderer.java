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
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import xyz.peatral.arcaneforces.Main;

public class ShrineHoloRenderer extends EntityRenderer<ShrineHoloEntity> implements RenderLayerParent<ShrineHoloEntity, ShrineHoloModel<ShrineHoloEntity>> {

    private final ShrineHoloModel<ShrineHoloEntity> model;

    public ShrineHoloRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShrineHoloModel<>(context.bakeLayer(ShrineHoloModel.LAYER_LOCATION));
    }

    @Override
    public void render(ShrineHoloEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
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

    public void renderFloatingText(PoseStack matrixStackIn, MultiBufferSource bufferIn, Component text, float x, float y, float z, int color, int combinedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(x, y, z);
        matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
        matrixStackIn.mulPose(new Quaternionf().rotationYXZ((float) Math.toRadians(camera.getYRot()), (float) Math.toRadians(-camera.getXRot()), 0.0F));
        Matrix4f matrix4f = matrixStackIn.last().pose();
        int backgroundColor = (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.76F) * 255) << 24;
        Font font = Minecraft.getInstance().font;
        float xOffset = -font.width(text) / 2f;
        font.drawInBatch(text, xOffset, 0, color, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, backgroundColor, combinedLightIn);
        matrixStackIn.popPose();
    }
}
