package net.satisfy.brewery.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.satisfy.brewery.util.rope.RopeHelper;

public class AttachRopeS2CPacket implements NetworkManager.NetworkReceiver {
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        int fromId = buf.readInt();
        int toId = buf.readInt();
        context.queue(() -> RopeHelper.createConnection(Minecraft.getInstance(), fromId, toId));
    }
}
