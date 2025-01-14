package net.satisfy.brewery.core.util.rope;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.HopsCropHeadBlock;
import net.satisfy.brewery.core.block.entity.rope.HangingRopeEntity;
import net.satisfy.brewery.core.block.entity.rope.RopeCollisionEntity;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import net.satisfy.brewery.core.networking.BreweryNetworking;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.util.BreweryMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RopeConnection {
    public static final double VISIBLE_RANGE = 2048.0D;
    private final RopeKnotEntity from;
    private final Entity to;
    private final List<Integer> collisions = new ArrayList<>();
    private final List<Integer> hangingRopes = new ArrayList<>();
    public boolean removeSilently = false;
    private boolean alive = true;
    private int activeRopes;

    private RopeConnection(RopeKnotEntity from, Entity to, int activeRopes) {
        this.from = from;
        this.to = to;
        this.activeRopes = activeRopes;
    }

    @Nullable
    public static RopeConnection create(@NotNull RopeKnotEntity fromKnot, @NotNull Entity to) {
        return create(fromKnot, to, 0);
    }

    @Nullable
    public static RopeConnection create(@NotNull RopeKnotEntity fromKnot, @NotNull Entity to, int activeRopes) {
        RopeConnection connection = new RopeConnection(fromKnot, to, activeRopes);
        if (fromKnot.sameConnectionExist(connection)) return null;

        fromKnot.addConnection(connection);
        if (to instanceof RopeKnotEntity toKnot) {
            toKnot.addConnection(connection);
            connection.createCollision();
            connection.createHangingRopes();
        }
        if (fromKnot.level() instanceof ServerLevel serverLevel) {
            connection.sendAttachRopePacket(serverLevel);
        }
        return connection;
    }

    private static Set<ServerPlayer> getTrackingPlayers(ServerLevel serverLevel, RopeConnection connection) {
        Set<ServerPlayer> trackingPlayers = new HashSet<>();
        RopeKnotEntity from = connection.from();
        Entity to = connection.to();
        trackingPlayers.addAll(serverLevel.players().stream().filter((player) -> player.distanceToSqr(from.position().x(), from.position().y(), from.position().z()) <= VISIBLE_RANGE).toList());
        trackingPlayers.addAll(serverLevel.players().stream().filter((player) -> player.distanceToSqr(to.position().x(), to.position().y(), to.position().z()) <= VISIBLE_RANGE).toList());
        return trackingPlayers;
    }

    public RopeKnotEntity from() {
        return from;
    }

    public Entity to() {
        return to;
    }

    public boolean dead() {
        return !alive;
    }

    public int activeHangingRopes() {
        return this.activeRopes;
    }

    public Level getLevel() {
        return from.level();
    }

    public double getSquaredDistance() {
        return this.from.distanceToSqr(to);
    }

    public Vec3 getConnectionVec(float tickDelta) {
        Vec3 fromPos = from.position().add(from.getLeashOffset());
        Vec3 toPos = to.getRopeHoldPosition(tickDelta);
        return toPos.subtract(fromPos);
    }

    public void setActive(boolean active, int id) {
        int index = this.hangingRopes.indexOf(id);
        if (index >= 0) {
            if (active) {
                this.activeRopes &= ~(1 << index);
            } else {
                this.activeRopes |= (1 << index);
            }
        }
    }

    private void sendAttachRopePacket(ServerLevel serverLevel) {
        Set<ServerPlayer> trackingPlayers = getTrackingPlayers(serverLevel, this);

        for (ServerPlayer player : trackingPlayers) {
            FriendlyByteBuf buf = BreweryNetworking.createPacketBuf();
            buf.writeInt(from.getId());
            buf.writeInt(to.getId());
            NetworkManager.sendToPlayer(player, BreweryNetworking.ATTACH_ROPE_S2C_ID, buf);
        }
    }

    private void createCollision() {
        if (!collisions.isEmpty()) return;
        if (from.level().isClientSide()) return;

        float distance = from.distanceTo(to);
        float step = (EntityTypeRegistry.ROPE_COLLISION.get().getWidth() * 2.5F) / distance;
        float centerHoldout = EntityTypeRegistry.ROPE_COLLISION.get().getWidth() / distance;

        Vec3 startPos = from.position().add(from.getLeashOffset());
        Vec3 endPos = to.position().add(to.getLeashOffset(to.getEyeHeight()));

        for (double v = step; v < 0.5F - centerHoldout; v += step) {
            Entity fromCollider = spawnCollision(startPos, endPos, v);
            if (fromCollider != null) collisions.add(fromCollider.getId());
            Entity toCollider = spawnCollision(endPos, startPos, v);
            if (toCollider != null) collisions.add(toCollider.getId());
        }

        Entity centerCollider = spawnCollision(startPos, endPos, 0.5);
        if (centerCollider != null) collisions.add(centerCollider.getId());
    }

    @Nullable
    private Entity spawnCollision(Vec3 startPos, Vec3 endPos, double v) {
        assert from.level() instanceof ServerLevel;
        Vec3 ropeVec = endPos.subtract(startPos);
        Vec3 currentVec = ropeVec.scale(v);
        Vec3 currentPos = startPos.add(currentVec);

        double y = RopeHelper.getYHanging(currentVec.length(), endPos.subtract(startPos));
        y -= EntityTypeRegistry.ROPE_COLLISION.get().getHeight() / 2;

        RopeCollisionEntity collisionEntity = RopeCollisionEntity.create(from.level(), currentPos.x(), currentPos.y() + y, currentPos.z(), this);
        if (from.level().addFreshEntity(collisionEntity)) {
            return collisionEntity;
        } else {
            return null;
        }
    }

    private void createHangingRopes() {
        if (from.level().isClientSide()) return;

        Vec3 startPos = from.position().add(from.getLeashOffset());
        Vec3 endPos = to.position().add(to.getLeashOffset(to.getEyeHeight()));
        Vec3 ropeVec = endPos.subtract(startPos);

        int i = 0;
        Set<BlockPos> blockPositions = BreweryMath.lineIntersection(this);
        for (BlockPos blockPos : blockPositions) {
            BlockPos currentPos = blockPos.subtract(from.getPos());
            Vec3 currentVec = new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ());
            double y = RopeHelper.getYHanging(currentVec.length(), ropeVec);
            y -= EntityTypeRegistry.HANGING_ROPE.get().getHeight();

            boolean active = (this.activeRopes & (1 << i)) == 0;
            HangingRopeEntity hangingRopeEntity = HangingRopeEntity.create(from.level(), blockPos.getX(), startPos.add(currentVec).y + y, blockPos.getZ(), this, active);
            hangingRopeEntity.setTicksFrozen((byte) 0);
            boolean added = from.level().addFreshEntity(hangingRopeEntity);
            if (added) {
                this.hangingRopes.add(hangingRopeEntity.getId());
            }
            i++;
        }
    }

    public boolean needsBeDestroyed() {
        return from.isRemoved() || to.isRemoved();
    }

    public void destroy(boolean mayDrop) {
        if (!alive) return;
        this.alive = false;

        Level level = from.level();
        if (level.isClientSide()) return;

        boolean drop = mayDrop;
        if (to instanceof Player player && player.isCreative()) drop = false;
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) drop = false;

        if (drop) {
            ItemStack stack = new ItemStack(ObjectRegistry.ROPE.get());
            if (to instanceof Player player) {
                player.addItem(stack);
            } else {
                Vec3 middle = BreweryMath.middleOf(from.position(), to.position());
                ItemEntity itemEntity = new ItemEntity(level, middle.x, middle.y, middle.z, stack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        destroyCollision();
        destroyHangingRopes();
        if (from.level() instanceof ServerLevel serverLevel && !from.isRemoved() && !to.isRemoved()) {
            sendDetachChainPacket(serverLevel);
        }
    }

    private void sendDetachChainPacket(ServerLevel serverLevel) {
        Set<ServerPlayer> trackingPlayers = getTrackingPlayers(serverLevel, this);

        for (ServerPlayer player : trackingPlayers) {
            FriendlyByteBuf buf = BreweryNetworking.createPacketBuf();
            buf.writeInt(from.getId());
            buf.writeInt(to.getId());
            NetworkManager.sendToPlayer(player, BreweryNetworking.DETACH_ROPE_S2C_ID, buf);
        }
    }

    private void destroyCollision() {
        for (Integer entityId : this.collisions) {
            Entity e = from.level().getEntity(entityId);
            if (e instanceof RopeCollisionEntity) {
                e.discard();
            }
        }
        collisions.clear();
    }

    private void destroyHangingRopes() {
        for (Integer entityId : this.hangingRopes) {
            Entity entity = from.level().getEntity(entityId);
            if (entity instanceof HangingRopeEntity) {
                entity.discard();
            }
        }
        if (from.level() instanceof ServerLevel serverLevel) {
            Set<BlockPos> blockPositions = BreweryMath.lineIntersection(this);
            for (BlockPos blockPos : blockPositions) {
                HangingRopeEntity.notifyBlock(blockPos, serverLevel, HopsCropHeadBlock.getHeadBlock());
            }
        }
        collisions.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RopeConnection that)) return false;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) || Objects.equals(from, that.to) && Objects.equals(to, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
