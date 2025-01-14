package net.satisfy.brewery.core.mixin.rope;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import net.satisfy.brewery.core.util.rope.RopeConnection;
import net.satisfy.brewery.core.networking.BreweryNetworking;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "sendPairingData", at = @At("TAIL"))
    private void sendPackages(ServerPlayer serverPlayer, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        if (this.entity instanceof RopeKnotEntity knot) {
            FriendlyByteBuf buf = BreweryNetworking.createPacketBuf();
            Set<RopeConnection> connections = knot.getConnections();
            IntList ids = new IntArrayList(connections.size());
            for (RopeConnection connection : connections) {
                if (connection.from() == knot) {
                    ids.add(connection.to().getId());
                }
            }
            if (!ids.isEmpty()) {
                buf.writeInt(knot.getId());
                buf.writeVarIntArray(ids.toIntArray());
                consumer.accept(new ClientboundCustomPayloadPacket(BreweryNetworking.SYNC_ROPE_S2C_ID, buf));
            }
        }
    }
}
