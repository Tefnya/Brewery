package net.satisfy.brewery.core.block.entity.rope;

import dev.architectury.extensions.network.EntitySpawnExtension;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.core.block.HopsCropBlock;
import net.satisfy.brewery.core.networking.BreweryNetworking;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.util.rope.RopeConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class HangingRopeEntity extends Entity implements IRopeEntity, EntitySpawnExtension {
    public static final int MAX_LENGTH = 8;
    @Nullable
    private RopeConnection connection;
    private boolean active;
    @Environment(EnvType.CLIENT)
    private int length;

    private int checkTimer = 50;

    public HangingRopeEntity(EntityType<? extends HangingRopeEntity> entityType, Level level) {
        super(entityType, level);
    }

    private HangingRopeEntity(Level level, double x, double y, double z, @NotNull RopeConnection connection, boolean active) {
        this(EntityTypeRegistry.HANGING_ROPE.get(), level);
        this.connection = connection;
        this.active = active;
        this.setPos(x, y, z);
    }

    public static HangingRopeEntity create(Level level, double x, double y, double z, RopeConnection connection) {
        return create(level, x, y, z, connection, true);
    }

    public static HangingRopeEntity create(Level level, double x, double y, double z, RopeConnection connection, boolean active) {
        return new HangingRopeEntity(level, x, y, z, connection, active);
    }

    public static void notifyBlock(BlockPos blockPos, ServerLevel serverLevel, Block block) {
        for (int below = 0; below < HangingRopeEntity.MAX_LENGTH; below++) {
            BlockPos belowPos = blockPos.below(below);
            BlockState blockState = serverLevel.getBlockState(belowPos);
            if (blockState.is(block)) {
                serverLevel.scheduleTick(belowPos, blockState.getBlock(), 1);
                return;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public Vec3 getRopeVec() {
        return new Vec3(0, -length, 0);
    }

    public boolean active() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void tick() {
        if (level().isClientSide()) {
            if (checkTimer++ >= 50) {
                checkTimer = 0;
                checkLength();
            }
            return;
        }
        if (connection != null && connection.needsBeDestroyed()) connection.destroy(true);

        if (connection == null || connection.dead()) {
            remove(RemovalReason.DISCARDED);
        }
    }

    @Environment(EnvType.CLIENT)
    private void checkLength() {
        BlockPos blockPos = this.blockPosition();
        int length = 0;
        while (!this.level().getBlockState(blockPos.below(length + 1)).isSolid() && length < MAX_LENGTH) {
            length++;
        }
        this.length = length;
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean changed = (!this.active && itemStack.is(ObjectRegistry.ROPE.get())) || (this.active && IRopeEntity.canDestroyWith(itemStack));
        if (changed) {
            this.active = !this.active;
            if (connection != null && !player.level().isClientSide) connection.setActive(this.active, this.getId());
            if (player.level() instanceof ServerLevel serverLevel) {
                sendChangePacket(serverLevel);
                if (!this.active) {
                    notifyBlock(this.blockPosition(), serverLevel, HopsCropBlock.getHeadBlock());
                }
            }
            player.playSound(this.active ? SoundEvents.LEASH_KNOT_PLACE : SoundEvents.LEASH_KNOT_BREAK, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void sendChangePacket(ServerLevel serverLevel) {
        List<ServerPlayer> trackingPlayers = serverLevel.players();
        for (ServerPlayer serverPlayer : trackingPlayers) {
            FriendlyByteBuf buf = BreweryNetworking.createPacketBuf();
            buf.writeInt(this.getId());
            buf.writeBoolean(this.active);
            NetworkManager.sendToPlayer(serverPlayer, BreweryNetworking.CHANGE_HANGING_ROPE_S2C_ID, buf);
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player) {
            hurt(this.damageSources().playerAttack(player), 0.0F);
        } else {
            playSound(SoundEvents.WOOL_HIT, 0.5F, 1.0F);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        InteractionResult result = IRopeEntity.onDamageFrom(this, damageSource);

        if (result.consumesAction()) {
            destroyConnections(result == InteractionResult.SUCCESS);
            return true;
        }
        return false;
    }

    //Override Stuff

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos((double) Mth.floor(x) + 0.5D, y, (double) Mth.floor(z) + 0.5D);
    }

    @Override
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0, EntityTypeRegistry.HANGING_ROPE.get().getHeight(), 0);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull Vec3 getRopeHoldPosition(float f) {
        return getPosition(f).add(getLeashOffset());
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return EntityTypeRegistry.HANGING_ROPE.get().getHeight() / 2;
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void destroyConnections(boolean mayDrop) {
        if (connection != null) connection.destroy(mayDrop);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    @Override
    public void saveAdditionalSpawnData(FriendlyByteBuf buf) {
        buf.writeBoolean(this.active);
    }

    @Override
    public void loadAdditionalSpawnData(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
    }
}
