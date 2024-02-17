package net.satisfy.brewery.block.barrel;

import de.cristelknight.doapi.common.block.FacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.block.brewingstation.BrewKettleBlock;
import net.satisfy.brewery.block.property.BrewMaterial;
import net.satisfy.brewery.entity.BigBarrelBlockEntity;
import net.satisfy.brewery.entity.BrewstationBlockEntity;
import net.satisfy.brewery.registry.BlockStateRegistry;
import net.satisfy.brewery.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BigBarrelBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF;
    public BigBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState());
    }

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        if (!(this instanceof BigBarrelMainBlock)) {
            BigBarrelBlockEntity blockEntity = getController(pos, getter);
            if (blockEntity != null) {
                Brewery.LOGGER.error("Is null (blockEntity)");
                return blockEntity.getBlockState().getBlock().getCloneItemStack(getter, pos, state);
            }
        }
        return super.getCloneItemStack(getter, pos, state);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        BigBarrelBlockEntity brewstationEntity = getController(blockPos, level);
        if (brewstationEntity != null) {
            brewstationEntity.getComponents().stream()
                    .filter(componentPos -> !componentPos.equals(blockPos))
                    .forEach(componentPos -> level.removeBlock(componentPos, false));
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }


    @Nullable
    protected BigBarrelBlockEntity getController(BlockPos centerPos, Level level) {
        return findController(centerPos, level);
    }

    @Nullable
    protected BigBarrelBlockEntity getController(BlockPos centerPos, BlockGetter getter) {
        return findController(centerPos, getter);
    }

    private <T extends BlockGetter> BigBarrelBlockEntity findController(BlockPos centerPos, T blockGetter) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockEntity blockEntity = blockGetter.getBlockEntity(centerPos.offset(x, 0, y));
                if (blockEntity instanceof BigBarrelBlockEntity brewstationEntity && brewstationEntity.isPartOf(centerPos)) {
                    return brewstationEntity;
                }
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
