package com.seres.realisticnetworkstorage;

import com.seres.realisticnetworkstorage.events.RNSRegistry;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageContainerScreen;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageController;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorScreen;
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
        ScreenRegistry.<BasicEnergyStorageController, BasicEnergyStorageContainerScreen>register
                (RNSRegistry.basicEnergyStorageScreen, (desc, inventory, title) -> new BasicEnergyStorageContainerScreen(desc, inventory.player, title));
        ScreenRegistry.<BasicGeneratorController, BasicGeneratorScreen>register
                (RNSRegistry.basicGeneratorScreen, (desc, inventory, title) -> new BasicGeneratorScreen(desc, inventory.player, title));
    }
}