package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class BlockEntityWithInventory extends BlockEntityWithGui implements ImplementedInventory
{
    protected DefaultedList<ItemStack> items;

    public BlockEntityWithInventory(BlockEntityType<?> type, EnergyTier tier, int inventorySize)
    {
        super(type, tier);
        this.items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        Inventories.toTag(tag, items);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, this.items);
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
