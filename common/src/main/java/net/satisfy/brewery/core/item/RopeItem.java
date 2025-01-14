package net.satisfy.brewery.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import org.jetbrains.annotations.NotNull;

public class RopeItem extends Item {

    public RopeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (player != null && (blockState.is(BlockTags.FENCES) || blockState.is(Blocks.TRIPWIRE_HOOK))) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            InteractionHand hand = useOnContext.getHand();
            RopeKnotEntity knot = RopeKnotEntity.getHopRopeKnotEntity(level, blockPos);
            if (knot != null) {
                if (knot.interact(player, hand) == InteractionResult.CONSUME) {
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }
            knot = RopeKnotEntity.create(level, blockPos);
            knot.setTicksFrozen((byte) 0);
            level.addFreshEntity(knot);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return knot.interact(player, hand);
        } else {
            return InteractionResult.PASS;
        }
    }
}
