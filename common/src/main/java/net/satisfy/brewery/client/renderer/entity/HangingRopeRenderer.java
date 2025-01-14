package net.satisfy.brewery.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.core.block.entity.rope.HangingRopeEntity;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.util.BreweryIdentifier;
import net.satisfy.brewery.core.util.BreweryMath;

@Environment(EnvType.CLIENT)
public class HangingRopeRenderer extends EntityRenderer<HangingRopeEntity> {
    private final RopeRender hopRopeRenderer = new RopeRender();

    public HangingRopeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(HangingRopeEntity entity, Frustum frustum, double d, double e, double f) {
        if (!entity.shouldRender(d, e, f)) {
            return false;
        }
        if (super.shouldRender(entity, frustum, d, e, f)) {
            return true;
        }
        Vec3 bottomPos = entity.position().add(entity.getRopeVec());
        AABB aABB = new AABB(bottomPos.x() - 2.0, bottomPos.y() - 2.0, bottomPos.y() - 2.0, bottomPos.x() + 2.0, bottomPos.y() + 2.0, bottomPos.z() + 2.0);
        return frustum.isVisible(aABB);
    }

    @Override
    public void render(HangingRopeEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, g, poseStack, multiBufferSource, i);
        if (!entity.active()) return;
        RenderType entityCutout = RenderType.entityCutoutNoCull(new BreweryIdentifier("textures/rope/rope.png"));

        Vec3 ropeVec = entity.getRopeVec();
        if (ropeVec.length() < 1) return;
        BlockPos blockPosOfStart = BreweryMath.ofFloored(entity.position());
        BlockPos blockPosOfEnd = BreweryMath.ofFloored(entity.position().add(ropeVec).add(new Vec3(0, 1, 0)));
        Level level = entity.level();
        int blockLightLevelOfStart = level.getBrightness(LightLayer.BLOCK, blockPosOfStart);
        int blockLightLevelOfEnd = level.getBrightness(LightLayer.BLOCK, blockPosOfEnd);
        int skylightLevelOfStart = level.getBrightness(LightLayer.SKY, blockPosOfStart);
        int skylightLevelOfEnd = level.getBrightness(LightLayer.SKY, blockPosOfEnd);

        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(entityCutout);
        poseStack.pushPose();
        poseStack.translate(0, EntityTypeRegistry.HANGING_ROPE.get().getHeight(), 0);
        hopRopeRenderer.render(vertexConsumer, poseStack, ropeVec, entity.getId(), blockLightLevelOfStart, blockLightLevelOfEnd, skylightLevelOfStart, skylightLevelOfEnd);
        poseStack.popPose();
    }

    @Override
    @SuppressWarnings("all")
    public ResourceLocation getTextureLocation(HangingRopeEntity entity) {
        return null;
    }
}
