package com.seres.realisticnetworkstorage.gui.basicgenerator;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class BasicGeneratorScreen extends CottonInventoryScreen<BasicGeneratorController>
{
    public BasicGeneratorScreen(BasicGeneratorController description, PlayerEntity player, Text title)
    {
        super(description, player, title);
    }
}
