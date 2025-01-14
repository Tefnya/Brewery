package net.satisfy.brewery.core.block.entity.rope;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IRopeEntity {

    static InteractionResult onDamageFrom(Entity self, DamageSource source) {
        if (self.isInvulnerableTo(source)) {
            return InteractionResult.FAIL;
        }
        if (self.level().isClientSide) {
            return InteractionResult.PASS;
        }

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return InteractionResult.SUCCESS;
        }
        if (source.getEntity() instanceof Player player) {
            if (canDestroyWith(player.getMainHandItem())) {
                return InteractionResult.sidedSuccess(!player.isCreative());
            }
        }

        if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
            self.playSound(SoundEvents.WOOL_HIT, 0.5F, 1.0F);
        }
        return InteractionResult.FAIL;
    }

    static boolean canDestroyWith(ItemStack item) {
        return item.is(Items.SHEARS);
    }

    void destroyConnections(boolean mayDrop);
}
