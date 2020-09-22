package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.blockentities.DiskAccessorBlockEntity;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class DiskAccessor extends BlockWithInventory
{
    public DiskAccessor(Settings settings, EnergyTier tier)
    {
        super(settings, tier);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new DiskAccessorBlockEntity(tier);
    }
}
