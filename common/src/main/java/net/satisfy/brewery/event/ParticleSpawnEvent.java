package net.satisfy.brewery.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class ParticleSpawnEvent implements PlayerEvent.AttackEntity {
    @Override
    public EventResult attack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            if (target instanceof LivingEntity) {
                level.addParticle(ParticleTypes.EXPLOSION, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 0, 0, 0);
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 0, 0, 0);


                return EventResult.pass();
            }
        }

        return EventResult.pass();
    }
}
