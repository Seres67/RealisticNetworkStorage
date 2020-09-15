/*
 * This file is part of Seres67, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 Seres67
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class BlockEntityWithEnergy extends BlockEntity
{
    private final EnergyTier tier;
    private double energyStored = 0;
    private double max = 10000;

    public BlockEntityWithEnergy(BlockEntityType<?> type, EnergyTier tier)
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
