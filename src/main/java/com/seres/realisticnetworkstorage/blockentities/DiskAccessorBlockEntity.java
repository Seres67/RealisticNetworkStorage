package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.diskaccessor.DiskAccessorController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class DiskAccessorBlockEntity extends BlockEntityWithInventory
{
    public DiskAccessorBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.DISK_ACCESSOR, tier, 36);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new DiskAccessorController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
