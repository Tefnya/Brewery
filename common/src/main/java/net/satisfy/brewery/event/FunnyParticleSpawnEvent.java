package net.satisfy.brewery.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class FunnyParticleSpawnEvent implements PlayerEvent.AttackEntity {
    @Override
    public EventResult attack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            if (target instanceof LivingEntity) {
                ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
                CompoundTag fireworkNbt = new CompoundTag();
                fireworkNbt.putInt("Flight", 3);
                fireworkStack.getOrCreateTagElement("Fireworks").put("Explosions", fireworkNbt);

                FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(level, fireworkStack, (LivingEntity) target);
                level.addFreshEntity(fireworkRocket);

                if (!level.isClientSide) {
                    target.setDeltaMovement(target.getDeltaMovement().add(0, 1.5, 0));
                }

                return EventResult.pass();
            }
        }

        return EventResult.pass();
    }
}