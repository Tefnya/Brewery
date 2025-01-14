package net.satisfy.brewery.core.block;

import net.satisfy.brewery.core.registry.SoundEventRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BrewKettleBlock extends BrewingstationBlock implements EntityBlock {
    public static final EnumProperty<Liquid> LIQUID;
    public static final Map<Direction, VoxelShape> SHAPE;
    private static final Supplier<VoxelShape> voxelShapeSupplier;

    static {
        LIQUID = BlockStateRegistry.LIQUID;
        voxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.box(0, 0, 0.125, 0.875, 0.125, 1));
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0, 1, 1, 0.125));
            shape = Shapes.or(shape, Shapes.box(0.875, 0.125, 0.125, 1, 1, 1));
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0.125, 0.125, 1, 1));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.125, 0.9375, 0.875, 1, 1));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.5625, 0.09375, 0.875, 0.5625, 0.96875));
            return shape;
        };
        SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
            }
        });
    }

    private final BrewMaterial brewMaterial;

    public BrewKettleBlock(BrewMaterial brewMaterial, Properties properties) {
        super(properties);
        this.brewMaterial = brewMaterial;
        this.registerDefaultState(this.defaultBlockState().setValue(MATERIAL, brewMaterial).setValue(LIQUID, Liquid.EMPTY));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (interactionHand == InteractionHand.OFF_HAND) return InteractionResult.CONSUME;
        if (level.isClientSide) return InteractionResult.CONSUME;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.getBlockEntity(blockPos) instanceof BrewstationBlockEntity brewKettleEntity) {
            if (itemStack.isEmpty()) { //EMPTY
                ItemStack returnStack = brewKettleEntity.removeIngredient();
                if (returnStack != null) {
                    player.addItem(returnStack);
                    level.playSound(null, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    level.sendBlockUpdated(blockPos, blockState, blockState, UPDATE_CLIENTS);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.CONSUME;
            }
            if (itemStack.getItem() == ObjectRegistry.BEER_MUG.get().asItem()) { //BEER
                if (blockState.getValue(LIQUID) == Liquid.BEER) {
                    ItemStack beerStack = brewKettleEntity.getBeer();
                    if (beerStack != null) {
                        player.addItem(beerStack);
                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                            if (itemStack.isEmpty()) {
                                player.getInventory().removeItem(itemStack);
                            }
                            level.playSound(null, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                            level.sendBlockUpdated(blockPos, blockState, blockState, UPDATE_CLIENTS);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }
            if (itemStack.getItem() == Items.WATER_BUCKET) { //WATER_BUCKET
                if (blockState.getValue(LIQUID) == Liquid.EMPTY || blockState.getValue(LIQUID) == Liquid.DRAINED) {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(LIQUID, Liquid.FILLED));
                    level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
                    }
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
            if (itemStack.getItem() == Items.BUCKET) { //BUCKET
                Liquid liquid = blockState.getValue(LIQUID);
                if (liquid == Liquid.FILLED || liquid == Liquid.OVERFLOWING) {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(LIQUID, liquid == Liquid.OVERFLOWING ? Liquid.FILLED : Liquid.EMPTY));
                    level.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.isCreative()) {

                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(player.getItemInHand(interactionHand),player,new ItemStack(Items.WATER_BUCKET)));
                    }
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
            if (blockState.getValue(LIQUID) != Liquid.BEER) {
                InteractionResult interactionResult = brewKettleEntity.addIngredient(itemStack);
                if (interactionResult == InteractionResult.SUCCESS) {
                    level.playSound(null, blockPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);
                    level.sendBlockUpdated(blockPos, blockState, blockState, UPDATE_CLIENTS);
                }
                return interactionResult;
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(LIQUID) == Liquid.OVERFLOWING) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY() + 0.95;
            double z = blockPos.getZ() + 0.5;

            if (randomSource.nextDouble() < 0.3D) {
                level.playLocalSound(x, y, z, SoundEventRegistry.BREWSTATION_KETTLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            double offset = 0.2;
            double offsetX = randomSource.nextDouble() * offset - offset / 2;
            double offsetY = randomSource.nextDouble() * 0.1D;
            double offsetZ = randomSource.nextDouble() * offset - offset / 2;

            level.addParticle(ParticleTypes.BUBBLE, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.BUBBLE_POP, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BrewstationBlockEntity brewstationEntity) {
                Containers.dropContents(world, pos, brewstationEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        BrewMaterial material = state.getValue(MATERIAL);
        switch (material) {
            case COPPER:
                return new ItemStack(ObjectRegistry.COPPER_BREWINGSTATION.get());
            case WOOD:
                return new ItemStack(ObjectRegistry.WOODEN_BREWINGSTATION.get());
            case NETHERITE:
                return new ItemStack(ObjectRegistry.NETHERITE_BREWINGSTATION.get());
            default:
                return super.getCloneItemStack(getter, pos, state);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos mainPos = blockPlaceContext.getClickedPos();
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState == null) return null;
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = mainPos.relative(facing.getOpposite());
        BlockPos sidePos = mainPos.relative(facing.getCounterClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
        BlockPos topPos = diagonalPos.above();
        boolean placeable = canPlace(level, backPos, sidePos, diagonalPos, topPos);
        return placeable ? blockState : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = blockPos.relative(facing.getOpposite());
        BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
        BlockPos topPos = diagonalPos.above();
        if (!canPlace(level, backPos, sidePos, diagonalPos, topPos)) return;
        level.setBlock(backPos, ObjectRegistry.BREW_TIMER.get().defaultBlockState().setValue(FACING, facing).setValue(MATERIAL, this.brewMaterial), 3);
        level.setBlock(sidePos, ObjectRegistry.BREW_WHISTLE.get().defaultBlockState().setValue(FACING, facing).setValue(MATERIAL, this.brewMaterial), 3);
        level.setBlock(diagonalPos, ObjectRegistry.BREW_OVEN.get().defaultBlockState().setValue(FACING, facing).setValue(MATERIAL, this.brewMaterial), 3);

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BrewstationBlockEntity brewKettleEntity) {
            brewKettleEntity.setComponents(blockPos, backPos, sidePos, diagonalPos);
        }
    }

    private boolean canPlace(Level level, BlockPos... blockPoses) {
        for (BlockPos blockPos : blockPoses) {
            if (!level.getBlockState(blockPos).isAir()) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BrewstationBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof BlockEntityTicker<?>) {
                ((BlockEntityTicker<T>) blockEntity).tick(world, pos, state1, blockEntity);
            }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIQUID);
    }
}
