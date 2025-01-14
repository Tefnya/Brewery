package net.satisfy.brewery.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.client.model.RopeKnotEntityModel;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import net.satisfy.brewery.core.registry.ModelRegistry;
import net.satisfy.brewery.core.util.BreweryIdentifier;
import net.satisfy.brewery.core.util.BreweryMath;
import net.satisfy.brewery.core.util.rope.RopeConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RopeKnotRenderer extends EntityRenderer<RopeKnotEntity> {
    private static final ResourceLocation KNOT_TEXTURE = new BreweryIdentifier("textures/rope/rope_knot.png");
    private final RopeRender hopRopeRenderer = new RopeRender();
    private final RopeKnotEntityModel<RopeKnotEntity> model;

    public RopeKnotRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RopeKnotEntityModel<>(context.bakeLayer(ModelRegistry.ROPE_KNOT));
    }

    @Override
    public boolean shouldRender(RopeKnotEntity entity, Frustum frustum, double d, double e, double f) {
        if (entity.noCulling) return true;
        for (RopeConnection connection : entity.getConnections()) {
            if (connection.from() != entity) continue;
            if (connection.to() instanceof Player) return true;
            else if (connection.to().shouldRender(d, e, f)) return true;
        }
        return super.shouldRender(entity, frustum, d, e, f);
    }

    @Override
    public void render(RopeKnotEntity entity, float f, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (entity.shouldRenderKnot()) {
            poseStack.pushPose();
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(KNOT_TEXTURE));
            this.model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }

        Set<RopeConnection> connections = entity.getConnections();
        for (RopeConnection connection : connections) {
            if (connection.from() != entity || connection.dead()) continue;
            this.renderRopeConnection(connection, tickDelta, poseStack, multiBufferSource);
        }
        super.render(entity, f, tickDelta, poseStack, multiBufferSource, light);
    }

    private void renderRopeConnection(RopeConnection connection, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider) {
        RopeKnotEntity fromKnot = connection.from();
        Entity toEntity = connection.to();
        Vec3 ropeVec = connection.getConnectionVec(tickDelta);

        RenderType entityCutout = RenderType.entityCutoutNoCull(new BreweryIdentifier("textures/rope/rope.png"));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(entityCutout);

        BlockPos blockPosOfStart = BreweryMath.ofFloored(fromKnot.getEyePosition(tickDelta));
        BlockPos blockPosOfEnd = BreweryMath.ofFloored(toEntity.getEyePosition(tickDelta));
        int blockLightLevelOfStart = fromKnot.level().getBrightness(LightLayer.BLOCK, blockPosOfStart);
        int blockLightLevelOfEnd = toEntity.level().getBrightness(LightLayer.BLOCK, blockPosOfEnd);
        int skylightLevelOfStart = fromKnot.level().getBrightness(LightLayer.SKY, blockPosOfStart);
        int skylightLevelOfEnd = toEntity.level().getBrightness(LightLayer.SKY, blockPosOfEnd);

        poseStack.pushPose();
        Vec3 leashOffset = fromKnot.getLeashOffset();
        poseStack.translate(leashOffset.x, leashOffset.y, leashOffset.z);
        hopRopeRenderer.render(vertexConsumer, poseStack, ropeVec, fromKnot.getId(), blockLightLevelOfStart, blockLightLevelOfEnd, skylightLevelOfStart, skylightLevelOfEnd);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(RopeKnotEntity entity) {
        return new ResourceLocation("");
    }
}
