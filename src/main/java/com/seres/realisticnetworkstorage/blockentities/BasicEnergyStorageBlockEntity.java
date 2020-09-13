package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class BasicEnergyStorageBlockEntity extends BasicEnergyGuiBlockEntity
{
    public BasicEnergyStorageBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.BASIC_ENERGY_STORAGE, tier);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new BasicEnergyStorageController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
