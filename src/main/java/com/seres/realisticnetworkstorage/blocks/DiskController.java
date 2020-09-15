package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.blockentities.DiskControllerBlockEntity;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class DiskController extends BlockWithInventory
{
    public DiskController(Settings settings, EnergyTier tier)
    {
        super(settings, tier);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new DiskControllerBlockEntity(tier);
    }
}
