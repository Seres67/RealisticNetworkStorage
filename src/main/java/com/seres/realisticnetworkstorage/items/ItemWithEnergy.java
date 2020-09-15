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

package com.seres.realisticnetworkstorage.items;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class ItemWithEnergy extends Item
{
    private double max;
    private EnergyTier tier;

    public ItemWithEnergy(Settings settings, double max, EnergyTier tier)
    {
        super(settings);
        this.max = max;
        this.tier = tier;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        assert world != null;
        tooltip.add(new TranslatableText("item.realisticnetworkstorage.basic_energy_item.tooltip", getStoredEnergy(stack), getMaxEnergy()));
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
