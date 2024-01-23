package net.satisfy.brewery.effect;

import net.satisfy.brewery.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HarddrinkingEffect extends MobEffect {
    public HarddrinkingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF00);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.hasEffect(MobEffectRegistry.DRUNK.get())) {
            livingEntity.removeEffect(MobEffectRegistry.DRUNK.get());
        }
        super.applyEffectTick(livingEntity, amplifier);
    }
}
