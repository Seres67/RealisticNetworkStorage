package com.seres.realisticnetworkstorage.gui.diskcontroller;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class DiskControllerScreen extends CottonInventoryScreen<DiskControllerGuiController>
{
    public DiskControllerScreen(DiskControllerGuiController description, PlayerEntity player, Text title)
    {
        super(description, player, title);
    }
}
