package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.util.ImplementedInventory;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class BasicGeneratorBlockEntity extends BasicEnergyGuiBlockEntity implements ImplementedInventory, Tickable
{
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public int fuelSlot = 0;
    public int burnTime;
    public int totalBurnTime = 0;
    ItemStack burnItem;

    public BasicGeneratorBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.BASIC_GENERATOR, tier);
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
    public void tick()
    {
        assert world != null;
        if (world.isClient)
            return;
        if (getStoredEnergy() < getMaxEnergy())
            if (burnTime > 0) {
                burnTime--;
                setStoredEnergy(getStoredEnergy() + 2D);
            }
        if (burnTime == 0) {
            burnTime = totalBurnTime = getItemBurnTime(items.get(fuelSlot));
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

    @Override
    public DefaultedList<ItemStack> getItems()
    {
        return items;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player)
    {
        return pos.isWithinDistance(player.getBlockPos(), 4.5);
    }
}
