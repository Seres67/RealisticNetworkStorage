package com.seres.realisticnetworkstorage.gui.basicenergystorage;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class BasicEnergyStorageContainerScreen extends CottonInventoryScreen<BasicEnergyStorageController>
{
    public BasicEnergyStorageContainerScreen(BasicEnergyStorageController gui, PlayerEntity player, Text title)
    {
        super(gui, player, title);
    }
}
