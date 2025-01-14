package net.satisfy.brewery.core.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

public class PacifyEffect extends MobEffect {
    public PacifyEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity instanceof Player) {
            AttributeInstance attribute = entity.getAttribute(Attributes.FOLLOW_RANGE);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getValue() * 0.75);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity instanceof Player) {
            AttributeInstance attribute = entity.getAttribute(Attributes.FOLLOW_RANGE);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getValue() / 0.75);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player) {
            entity.level().getEntitiesOfClass(EnderMan.class, entity.getBoundingBox().inflate(32.0D))
                    .forEach(enderman -> enderman.setTarget(null));
        }
    }
}
