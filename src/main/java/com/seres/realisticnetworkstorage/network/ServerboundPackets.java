package com.seres.realisticnetworkstorage.network;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import net.minecraft.util.Identifier;

public class ServerboundPackets
{
    public static final Identifier CHAT_MESSAGE_PACKET_ID = new Identifier(RealisticNetworkStorage.MODID, "chat_message");
}