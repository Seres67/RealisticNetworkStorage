package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerGuiController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class DiskControllerBlockEntity extends BlockEntityWithInventory
{
    public DiskControllerBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.DISK_CONTROLLER, tier, 4);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new DiskControllerGuiController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
