package net.satisfy.brewery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class BeerMugBlockEntity extends BlockEntity {
    public static final String FLOWER_KEY = "flower";
    private Item flower;

    public BeerMugBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BEER_MUG_BLOCK_ENTITY.get(), pos, state);
    }

    public Item getFlower() {
        return flower;
    }

    public void setFlower(Item flower) {
        this.flower = flower;
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        writeFlower(nbt, flower);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        flower = readFlower(nbt);
    }

    public void writeFlower(CompoundTag nbt, Item flower) {
        CompoundTag nbtCompound = new CompoundTag();
        if (flower != null) {
            flower.getDefaultInstance().save(nbtCompound);
        }
        nbt.put(FLOWER_KEY, nbtCompound);
    }

    public Item readFlower(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(FLOWER_KEY)) {
            CompoundTag nbtCompound = nbt.getCompound(FLOWER_KEY);
            if (!nbtCompound.isEmpty()) {
                return ItemStack.of(nbtCompound).getItem();
            }
        }
        return null;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void setChanged() {
        if (level instanceof ServerLevel serverLevel) {
            Packet<ClientGamePacketListener> updatePacket = getUpdatePacket();

            for (ServerPlayer player : GeneralUtil.tracking(serverLevel, getBlockPos())) {
                player.connection.send(updatePacket);
            }
        }
        super.setChanged();
    }
}
