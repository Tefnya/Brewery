package net.satisfy.brewery.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Villager.class)
public class VillagerDiscountMixin {

    @Inject(method = "updateSpecialPrices", at = @At("HEAD"))
    private void applyHasteDiscount(Player player, CallbackInfo ci) {
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            Villager villager = (Villager) (Object) this;
            for (MerchantOffer offer : villager.getOffers()) {
                int hasteLevel = Objects.requireNonNull(player.getEffect(MobEffects.DIG_SPEED)).getAmplifier();
                double discountForEffect = 0.1 * (hasteLevel + 1);
                int discount = (int) Math.floor(discountForEffect * offer.getBaseCostA().getCount());

                offer.addToSpecialPriceDiff(-Math.max(discount, 1));
            }
        }
    }
}