package net.satisfy.brewery.item;

import de.cristelknight.doapi.common.item.CustomHatItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.brewery.registry.ArmorMaterialRegistry;
import net.satisfy.brewery.util.BreweryClientUtil;
import net.satisfy.brewery.util.BreweryIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewfestHatRedItem extends CustomHatItem {


    public BrewfestHatRedItem(Properties settings) {
        super(ArmorMaterialRegistry.BREWFEST_LEATHER, EquipmentSlot.HEAD, settings);
    }

    @Override
    public ResourceLocation getTexture() {
        return new BreweryIdentifier("textures/entity/brewfest_hat_red.png");
    }

    @Override
    public Float getOffset() {
        return -2.0f;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
        if (world != null && world.isClientSide()) {
            BreweryClientUtil.appendTooltip(tooltip);
        }
    }
}