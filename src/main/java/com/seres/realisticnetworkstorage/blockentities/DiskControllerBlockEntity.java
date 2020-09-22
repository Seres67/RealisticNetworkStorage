package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerGuiController;
import com.seres.realisticnetworkstorage.items.disks.BaseDiskItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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

    public int getTotalSize()
    {
        int size = 0;
        for (ItemStack i : items)
            size += ((BaseDiskItem) i.getItem()).getSize();
        return size;
    }

    public int getNumberOfStacks()
    {
        int size = 0;
        for (ItemStack i : items)
            ++size;
        return size;
    }

    public int getDiskSize(int index)
    {
        return ((BaseDiskItem) items.get(index).getItem()).getSize();
    }
}
