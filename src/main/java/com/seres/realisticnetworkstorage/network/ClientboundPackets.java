package com.seres.realisticnetworkstorage.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.text.Text;

public class ClientboundPackets
{
    public static void init()
    {
        ClientSidePacketRegistry.INSTANCE.register(ServerboundPackets.CHAT_MESSAGE_PACKET_ID,
                (packetContext, attachedData) -> {
                    String message = attachedData.readString();
                    packetContext.getTaskQueue().execute(() -> packetContext.getPlayer().sendMessage(Text.of(message), false));
                });
    }
}
