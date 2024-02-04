package net.satisfy.brewery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.effect.alcohol.AlcoholManager;
import net.satisfy.brewery.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DrinkBlockItem extends BlockItem {
    private final MobEffect effect;
    private final int baseDuration;
    public DrinkBlockItem(MobEffect effect, int duration, Block block, Properties settings) {
        super(block, settings);
        this.effect = effect;
        this.baseDuration = duration;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack returnStack = super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof Player player && !player.isCreative()) {
            player.addItem(new ItemStack(ObjectRegistry.BEER_MUG.get()));
        }
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            AlcoholManager.drinkAlcohol(serverPlayer);

            if (itemStack.hasTag() && Objects.requireNonNull(itemStack.getTag()).contains("brewery.beer_quality")) {
                int quality = itemStack.getTag().getInt("brewery.beer_quality");
                MobEffectInstance effectInstance = calculateEffectForQuality(quality);
                serverPlayer.addEffect(effectInstance);
            } else {
                MobEffectInstance effectInstance = new MobEffectInstance(effect, baseDuration , 0);
                serverPlayer.addEffect(effectInstance);
            }
        }

        return returnStack;
    }

    @NotNull
    private MobEffectInstance calculateEffectForQuality(int quality) {
        int durationMultiplier = 1;
        int effectLevel = switch (quality) {
            case 2 -> {
                durationMultiplier = 3;
                yield 2;
            }
            case 3 -> {
                durationMultiplier = 5;
                yield 3;
            }
            default -> 1;
        };

        return new MobEffectInstance(effect, baseDuration * durationMultiplier, effectLevel - 1);
    }


    public static void addQuality(ItemStack itemStack, int quality) {
        CompoundTag nbtData = new CompoundTag();
        nbtData.putInt("brewery.beer_quality", Math.min(Math.max(quality, 0), 3));
        itemStack.setTag(nbtData);
    }

    public void addCount(ItemStack resultSack, int solved) {
        resultSack.setCount(solved);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        int beerQuality = stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("brewery.beer_quality") ? stack.getTag().getInt("brewery.beer_quality") : 1;
        int durationMultiplier = 1;
        int effectLevel = switch (beerQuality) {
            case 2 -> {
                durationMultiplier = 3;
                yield 2;
            }
            case 3 -> {
                durationMultiplier = 5;
                yield 3;
            }
            default -> 1;
        };

        if (this.effect != null) {
            MutableComponent effectName = Component.translatable(this.effect.getDescriptionId());
            if (effectLevel > 1) {
                effectName.append(" ").append(Component.translatable("potion.potency." + (effectLevel - 1))); // Add level next to the effect name.
            }
            MutableComponent effectDuration = Component.literal(" (" + MobEffectUtil.formatDuration(new MobEffectInstance(this.effect, this.baseDuration * durationMultiplier), 1.0f) + ")");
            tooltip.add(effectName.append(effectDuration).withStyle(this.effect.getCategory().getTooltipFormatting()));
        } else {
            tooltip.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }

        if (beerQuality > 1) {
            tooltip.add(Component.translatable("tooltip.brewery.beer_quality", beerQuality).withStyle(ChatFormatting.GOLD));
        }

        tooltip.add(Component.translatable("tooltip.brewery.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

}
