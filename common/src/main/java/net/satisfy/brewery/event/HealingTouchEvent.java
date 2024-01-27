package net.satisfy.brewery.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.satisfy.brewery.registry.MobEffectRegistry;
import org.jetbrains.annotations.Nullable;

public class HealingTouchEvent implements PlayerEvent.AttackEntity {
    @Override
    public EventResult attack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (player.hasEffect(MobEffectRegistry.HEALINGTOUCH.get())) {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).heal(6.0f);
                level.addParticle(ParticleTypes.HEART, target.getX(), target.getY() + target.getBbHeight() / 2.5, target.getZ(), 0, 0, 0);
                level.addParticle(ParticleTypes.HEART, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 0, 0, 0);
                level.addParticle(ParticleTypes.HEART, target.getX(), target.getY() + target.getBbHeight() / 1.5, target.getZ(), 0, 0, 0);

                return EventResult.interruptFalse();
            }
        }

        return EventResult.pass();
    }
}