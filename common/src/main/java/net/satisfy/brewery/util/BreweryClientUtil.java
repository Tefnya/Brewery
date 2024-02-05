package net.satisfy.brewery.util;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.satisfy.brewery.item.IBrewfestArmorSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BreweryClientUtil {
    public static <T extends BlockEntity> void renderBlock(BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, T entity) {
        Minecraft.getInstance()
                .getBlockRenderer()
                .renderSingleBlock(state, matrices, vertexConsumers, BreweryUtil.getLightLevel(entity.getLevel(), entity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }



    public static void registerColorArmor(Item item, int defaultColor) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> tintIndex > 0 ? -1 : getColor(stack, defaultColor), item);
    }

    private static int getColor(ItemStack itemStack, int defaultColor) {
        CompoundTag displayTag = itemStack.getTagElement("display");
        if (displayTag != null && displayTag.contains("color", Tag.TAG_ANY_NUMERIC))
            return displayTag.getInt("color");
        return defaultColor;
    }

}