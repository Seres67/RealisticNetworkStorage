package com.seres.realisticnetworkstorage;

import com.seres.realisticnetworkstorage.network.ClientboundPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealisticNetworkStorageClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientboundPackets.init();
    }
}