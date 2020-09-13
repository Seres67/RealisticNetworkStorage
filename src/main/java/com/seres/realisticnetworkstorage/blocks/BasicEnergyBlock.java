package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BasicEnergyBlock extends BlockWithEntity implements BlockEntityProvider
{
    protected EnergyTier tier;

    public BasicEnergyBlock(Settings settings, EnergyTier tier)
    {
        super(settings);
        this.tier = tier;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return null;
    }
}
