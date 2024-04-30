package net.satisfy.brewery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cristelknight.doapi.client.ClientUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.block.BeerMugFlowerPotBlock;
import net.satisfy.brewery.entity.BeerMugBlockEntity;

@SuppressWarnings("unused")
public class BeerMugBlockEntityRenderer implements BlockEntityRenderer<BeerMugBlockEntity> {

    public BeerMugBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BeerMugBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.hasLevel()) {
            return;
        }
        BlockState selfState = entity.getBlockState();
        if (selfState.getBlock() instanceof BeerMugFlowerPotBlock) {
            Item item = entity.getFlower();
            matrices.pushPose();
            if (item instanceof BlockItem) {
                BlockState state = ((BlockItem) item).getBlock().defaultBlockState();
                matrices.translate(0f, 0.4f, 0f);
                ClientUtil.renderBlock(state, matrices, vertexConsumers, entity);
            }
        }
        matrices.popPose();
    }
}