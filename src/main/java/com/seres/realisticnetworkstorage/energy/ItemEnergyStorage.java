package com.seres.realisticnetworkstorage.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class ItemEnergyStorage extends Item
{
    private double max;
    private EnergyTier tier;

    public ItemEnergyStorage(Settings settings, double max, EnergyTier tier)
    {
        super(settings);
        this.max = max;
        this.tier = tier;
    }

    public double getMaxEnergy()
    {
        return max;
    }

    public void setMaxEnergy(double max)
    {
        this.max = max;
    }

    public EnergyTier getTier()
    {
        return tier;
    }

    public void setTier(EnergyTier tier)
    {
        this.tier = tier;
    }

    public double getStoredEnergy(ItemStack stack)
    {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getDouble("energyStored");
    }

    public void setStoredEnergy(ItemStack stack, double amount)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putDouble("energyStored", amount);
        stack.setTag(tag);
    }

    public double getMaxOutput()
    {
        return getTier().getMaxOutput();
    }

    public double getMaxInput()
    {
        return getTier().getMaxInput();
    }
}
