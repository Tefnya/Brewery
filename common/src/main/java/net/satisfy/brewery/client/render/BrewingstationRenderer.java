package net.satisfy.brewery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.block.brewingstation.BrewingstationBlock;
import net.satisfy.brewery.entity.BrewstationBlockEntity;
import net.satisfy.brewery.util.BreweryUtil;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class BrewingstationRenderer implements BlockEntityRenderer<BrewstationBlockEntity> {
    public BrewingstationRenderer(BlockEntityRendererProvider.Context ctx) {

    }

    @Override
    public void render(BrewstationBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel()) {
            return;
        }
        BlockState selfState = blockEntity.getBlockState();
        if (selfState.getBlock() instanceof BrewingstationBlock) {
            List<ItemStack> ingredients = blockEntity.getIngredient();
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(1.0f, 0.3F, 1.0f);

            Random r = new Random(blockEntity.getBlockPos().hashCode());
            Vec3 baseVector = new Vec3(0.125, 0.3, 0);

            int itemCount = ingredients.size();

            if (itemCount == 1)
                baseVector = new Vec3(0, 0.3, 0);

            float anglePartition = 360f / itemCount;
            for (ItemStack stack : ingredients) {
                poseStack.pushPose();

                Vec3 itemPosition = rotate(baseVector, anglePartition * itemCount, Direction.Axis.Y);
                poseStack.translate(itemPosition.x, itemPosition.y, itemPosition.z);

                poseStack.mulPose(Vector3f.YP.rotationDegrees(anglePartition * itemCount + 35));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(65));

                for (int k = 0; k <= stack.getCount() / 8; k++) {
                    poseStack.pushPose();

                    Vec3 vec = offsetRandomly(Vec3.ZERO, r, 1 / 16f);

                    poseStack.translate(vec.x, vec.y, vec.z);
                    BreweryUtil.renderItem(stack, poseStack, multiBufferSource, blockEntity);
                    poseStack.popPose();
                }

                poseStack.popPose();

                itemCount--;
            }

            poseStack.popPose();
        }
    }

    private Vec3 rotate(Vec3 vec, double deg, Direction.Axis axis) {
        if (deg == 0)
            return vec;
        if (vec == Vec3.ZERO)
            return vec;

        float angle = (float) (deg / 180f * Math.PI);
        double sin = Mth.sin(angle);
        double cos = Mth.cos(angle);
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        if (axis == Direction.Axis.X)
            return new Vec3(x, y * cos - z * sin, z * cos + y * sin);
        if (axis == Direction.Axis.Y)
            return new Vec3(x * cos + z * sin, y, z * cos - x * sin);
        if (axis == Direction.Axis.Z)
            return new Vec3(x * cos - y * sin, y * cos + x * sin, z);
        return vec;
    }

    private Vec3 offsetRandomly(Vec3 vec, Random r, float radius) {
        return new Vec3(vec.x + (r.nextFloat() - .5f) * 2 * radius, vec.y + (r.nextFloat() - .5f) * 2 * radius,
                vec.z + (r.nextFloat() - .5f) * 2 * radius);
    }
}
