package net.satisfy.brewery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.block.property.LineConnectingType;
import net.satisfy.brewery.entity.StorageBlockEntity;
import net.satisfy.brewery.registry.BlockStateRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SideboardBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<LineConnectingType> TYPE = BlockStateRegistry.LINE_CONNECTING_TYPE;

	private final SoundEvent openSound;
	private final SoundEvent closeSound;

	public SideboardBlock(Properties settings, SoundEvent openSound, SoundEvent closeSound) {
		super(settings);
		this.openSound = openSound;
		this.closeSound = closeSound;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, LineConnectingType.NONE));
	}

	public static final Map<Direction, VoxelShape> SHAPES = Util.make(new HashMap<>(), map -> {
		map.put(Direction.NORTH, Block.box(0,0,4, 16, 16, 16));
		map.put(Direction.SOUTH, Block.box(0,0,0, 16, 16, 12));
		map.put(Direction.WEST, Block.box(4,0,0, 16, 16, 16));
		map.put(Direction.EAST, Block.box(0,0,0, 12, 16, 16));
	});

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getHorizontalDirection().getOpposite();
		BlockState blockState = this.defaultBlockState().setValue(FACING, facing);

		Level world = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();

		LineConnectingType type = getType(blockState, world.getBlockState(clickedPos.relative(facing.getClockWise())), world.getBlockState(clickedPos.relative(facing.getCounterClockWise())));
		return blockState.setValue(TYPE, type);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (!world.isClientSide) {
			Direction facing = state.getValue(FACING);
			LineConnectingType type = getType(state, world.getBlockState(pos.relative(facing.getClockWise())), world.getBlockState(pos.relative(facing.getCounterClockWise())));

			if (state.getValue(TYPE) != type) {
				world.setBlock(pos, state.setValue(TYPE, type), 3);
			}
		}
	}

	public LineConnectingType getType(BlockState state, BlockState oneSide, BlockState otherSide) {
		boolean oneSideSame = oneSide.getBlock() == state.getBlock() && oneSide.getValue(FACING) == state.getValue(FACING);
		boolean otherSideSame = otherSide.getBlock() == state.getBlock() && otherSide.getValue(FACING) == state.getValue(FACING);

		if (oneSideSame && otherSideSame) {
			return LineConnectingType.MIDDLE;
		} else if (oneSideSame) {
			return LineConnectingType.LEFT;
		} else if (otherSideSame) {
			return LineConnectingType.RIGHT;
		}
		return LineConnectingType.NONE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof StorageBlockEntity blockEntity1) {
				player.openMenu(blockEntity1);
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Container) {
				Containers.dropContents(world, pos, (Container)blockEntity);
				world.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, world, pos, newState, moved);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageBlockEntity(pos, state, this.openSound, this.closeSound);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof StorageBlockEntity blockEntity1) {
				blockEntity1.setCustomName(itemStack.getHoverName());
			}
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TYPE);
	}

	public void playSound(Level world, BlockPos pos, boolean open) {
		world.playSound(null, pos, open ? this.openSound : this.closeSound, SoundSource.BLOCKS, 1.0f, 1.0f);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("tooltip.brewery.expandable").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
}