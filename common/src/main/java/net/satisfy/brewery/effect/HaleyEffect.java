package net.satisfy.brewery.effect;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HaleyEffect extends MobEffect {
    public HaleyEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player && !player.isOnGround() && !player.isFallFlying() && !player.isInWater()) {
            player.startFallFlying();
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }
}