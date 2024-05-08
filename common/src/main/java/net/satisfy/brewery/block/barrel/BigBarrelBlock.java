package net.satisfy.brewery.block.barrel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.satisfy.brewery.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class BigBarrelBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF;

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }

    public BigBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState());
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        if (!(this instanceof BigBarrelMainBlock)) {
            return ObjectRegistry.BARREL_MAIN.get().getCloneItemStack(getter, pos, state);
        }
        return super.getCloneItemStack(getter, pos, state);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
