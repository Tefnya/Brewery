package net.satisfy.brewery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.brewery.util.BreweryIdentifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StandardItem extends de.cristelknight.doapi.common.item.StandardItem {
    public StandardItem(Properties properties) {
        super(properties, new BreweryIdentifier("textures/standard/beer_standard.png"), () -> new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1, true, false, true));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.brewery.thankyou_1").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.brewery.thankyou_2").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.brewery.thankyou_4").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.brewery.thankyou_3").withStyle(ChatFormatting.GOLD));
    }
}