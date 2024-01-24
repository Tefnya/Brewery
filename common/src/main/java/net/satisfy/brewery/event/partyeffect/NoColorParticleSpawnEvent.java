package net.satisfy.brewery.event.partyeffect;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class NoColorParticleSpawnEvent implements PlayerEvent.AttackEntity {
    private final Random random = new Random();

    @Override
    public EventResult attack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            if (target instanceof LivingEntity entity) {
                for (int i = 0; i < 50; i++) {
                    float r = random.nextFloat();
                    float g = random.nextFloat();
                    float b = random.nextFloat();
                    level.addParticle(ParticleTypes.FIREWORK, entity.getX(), entity.getY() + entity.getBbHeight() / 2.0, entity.getZ(), r - 0.5, g - 0.5, b - 0.5);
                }

                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 1.0F);

                return EventResult.pass();
            }
        }

        return EventResult.pass();
    }
}