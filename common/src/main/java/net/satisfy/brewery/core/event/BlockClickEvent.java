package net.satisfy.brewery.core.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import net.satisfy.brewery.core.registry.ObjectRegistry;

public class BlockClickEvent implements InteractionEvent.RightClickBlock {
    @Override
    public EventResult click(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        if (player == null || player.isCrouching()) return EventResult.pass();
        Level level = player.level();

        BlockState blockState = level.getBlockState(pos);
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ObjectRegistry.ROPE.get()) || !RopeKnotEntity.canAttachTo(blockState) || blockState.is(BlockTags.FENCES) || blockState.is(Blocks.TRIPWIRE_HOOK)) {
            return EventResult.pass();
        }

        if (RopeKnotEntity.getHeldRopesInRange(player, Vec3.atCenterOf(pos)).isEmpty()) {
            return EventResult.pass();
        }

        RopeKnotEntity knot = RopeKnotEntity.getHopRopeKnotEntity(level, pos);
        if (knot != null) {
            if (knot.interact(player, hand) == InteractionResult.CONSUME) {
                return EventResult.interruptDefault();
            }
            return EventResult.pass();
        }

        knot = RopeKnotEntity.create(level, pos);
        knot.setTicksFrozen((byte) 0);
        level.addFreshEntity(knot);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        knot.interact(player, hand);
        return EventResult.interruptTrue();
    }
}
