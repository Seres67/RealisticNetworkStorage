package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class BasicEnergyBlockEntity extends BlockEntity
{
    private final EnergyTier tier;
    private double energyStored = 0;
    private double max = 10000;

    public BasicEnergyBlockEntity(BlockEntityType<?> type, EnergyTier tier)
    {
        super(type);
        this.tier = tier;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        energyStored = tag.getDouble("energyStored");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putDouble("energyStored", energyStored);
        return tag;
    }

    public void addEnergy(PlayerEntity player, double amount)
    {
        energyStored += amount;
        markDirty();
    }

    public double getStoredEnergy()
    {
        return energyStored;
    }

    public void setStoredEnergy(double amount)
    {
        energyStored = amount;
        markDirty();
    }

    public double getMaxOutput()
    {
        return tier.getMaxOutput();
    }

    public double getMaxInput()
    {
        return tier.getMaxInput();
    }

    public double getMaxEnergy()
    {
        return max;
    }

    public void setMaxEnergy(double value)
    {
        max = value;
    }

    public boolean canReceiveEnergy()
    {
        return true;
    }
}
