package net.satisfy.brewery.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.registry.BlockEntityRegistry;
import net.satisfy.brewery.util.BreweryUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BigBarrelBlockEntity extends BlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(54, ItemStack.EMPTY);
    private Set<BlockPos> components = new HashSet<>(4);

    public void setComponents(BlockPos... components) {
        if (components.length != 4) {
            Brewery.LOGGER.debug("Cant add components to BigBarrel. Should have 4 but only have {} parts.", components.length);
            return;
        }
        this.components.addAll(Arrays.asList(components));
    }

    public BigBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.BIG_BARREL_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.components = BreweryUtil.readBlockPoses(compoundTag);

    }

    public boolean isPartOf(BlockPos blockPos) {
        return components.contains(blockPos);
    }

    public @NotNull Set<BlockPos> getComponents() {
        return components;
    }


}

