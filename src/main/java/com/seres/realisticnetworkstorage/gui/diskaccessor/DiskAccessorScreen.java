package com.seres.realisticnetworkstorage.gui.diskaccessor;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class DiskAccessorScreen extends CottonInventoryScreen<DiskAccessorController>
{
    public DiskAccessorScreen(DiskAccessorController description, PlayerEntity player, Text title)
    {
        super(description, player, title);
    }
}
