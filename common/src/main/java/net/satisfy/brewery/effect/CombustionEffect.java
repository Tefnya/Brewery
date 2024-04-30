package net.satisfy.brewery.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Monster;

public class CombustionEffect extends MobEffect {
    public CombustionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getCommandSenderWorld() instanceof ServerLevel) {
            entity.blockPosition();
            entity.level().getEntities(entity, entity.getBoundingBox().inflate(4)).forEach(e -> {
                if (e instanceof Monster && entity.getRandom().nextFloat() < 0.02) {
                    e.setSecondsOnFire(5);
                }
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
