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

import com.seres.realisticnetworkstorage.client.ModConfig;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class BasicGeneratorBlockEntity extends BlockEntityWithInventory implements Tickable
{
    public int fuelSlot = 0;
    public int burnTime;
    public int totalBurnTime = 0;
    ItemStack burnItem;

    public BasicGeneratorBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.BASIC_GENERATOR, tier, 1);
    }

    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
            return 0;
        Map<Item, Integer> burnMap = AbstractFurnaceBlockEntity.createFuelTimeMap();
        if (burnMap.containsKey(stack.getItem()))
            return burnMap.get(stack.getItem()) / 4;
        return 0;
    }

    @Override
    public boolean canReceiveEnergy()
    {
        return false;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        burnTime = tag.getInt("burnTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putInt("burnTime", burnTime);
        return tag;
    }

    @Override
    public void tick()
    {
        assert world != null;
        if (world.isClient)
            return;
        if (getStoredEnergy() < getMaxEnergy())
            if (burnTime > 0) {
                burnTime -= 1 * ModConfig.getInstance().energyGainMultiplier;
                markDirty();
                setStoredEnergy(getStoredEnergy() + ModConfig.getInstance().energyGainMultiplier * 2);
            }
        if (burnTime == 0) {
            burnTime = totalBurnTime = getItemBurnTime(items.get(fuelSlot));
            markDirty();
            if (burnTime > 0) {
                burnItem = items.get(fuelSlot);
                if (items.get(fuelSlot).getCount() == 1) {
                    if (items.get(fuelSlot).getItem() == Items.LAVA_BUCKET || items.get(fuelSlot).getItem() instanceof BucketItem)
                        items.set(fuelSlot, new ItemStack(Items.BUCKET));
                    else
                        items.set(fuelSlot, ItemStack.EMPTY);
                } else {
                    ItemStack fuel = items.get(fuelSlot);
                    fuel.setCount(fuel.getCount() - 1);
                    items.set(fuelSlot, fuel);
                }
            }
        }

        if (getStoredEnergy() > 0) {
            for (Direction side : Direction.values()) {
                BasicEnergyStorageBlockEntity blockEntity = (BasicEnergyStorageBlockEntity) getWorld().getBlockEntity(getPos().offset(side));
                if (blockEntity == null || !blockEntity.canReceiveEnergy())
                    continue;
                double energyToTransfer = Math.min(getMaxOutput(), getStoredEnergy());
                double energyTransferred = Math.min(energyToTransfer, blockEntity.getMaxInput());
                double test = blockEntity.getStoredEnergy() + energyTransferred;

                if (test >= blockEntity.getMaxEnergy())
                    energyTransferred -= test - blockEntity.getMaxEnergy();
                setStoredEnergy(getStoredEnergy() - energyTransferred);
                blockEntity.setStoredEnergy(blockEntity.getStoredEnergy() + energyTransferred);
            }
        }
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new BasicGeneratorController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
