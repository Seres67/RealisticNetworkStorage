package com.seres.realisticnetworkstorage.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public abstract class BaseDiskItem extends Item
{
    public BaseDiskItem(Settings settings)
    {
        super(settings);
    }

    public abstract int getSize();

    public ItemStack getItem(ItemStack stack, int slot)
    {
        stack.getOrCreateTag();
        CompoundTag tagToGet = stack.getSubTag("item" + slot);
        return ItemStack.fromTag(tagToGet);
    }

    public void storeItem(ItemStack stackSource, int slot, ItemStack toStore)
    {
        stackSource.getOrCreateTag();
        CompoundTag tagToStore = new CompoundTag();
        toStore.toTag(tagToStore);
        stackSource.putSubTag("item" + slot, tagToStore);
    }
}
