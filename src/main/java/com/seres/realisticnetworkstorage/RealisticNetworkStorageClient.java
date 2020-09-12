package com.seres.realisticnetworkstorage;

import com.seres.realisticnetworkstorage.events.RNSRegistry;
import com.seres.realisticnetworkstorage.gui.BasicEnergyStorageContainerScreen;
import com.seres.realisticnetworkstorage.gui.BasicEnergyStorageController;
import com.seres.realisticnetworkstorage.network.ClientboundPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class RealisticNetworkStorageClient implements ClientModInitializer
{
    @SuppressWarnings("RedundantTypeArguments")
    @Override
    public void onInitializeClient()
    {
        ClientboundPackets.init();
        ScreenRegistry.<BasicEnergyStorageController, BasicEnergyStorageContainerScreen>register(
                RNSRegistry.energyStorageScreen,
                (desc, inventory, title) -> new BasicEnergyStorageContainerScreen(desc, inventory.player, title)
        );
    }
}