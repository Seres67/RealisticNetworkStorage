package com.seres.realisticnetworkstorage.items.disks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;

public abstract class BaseDiskItem extends Item
{
    private int stacks = 0;

    public BaseDiskItem(Settings settings)
    {
        super(settings);
    }

    public abstract int getSize();

    public ItemStack getItemAtSlot(ItemStack stack, int s)
    {
        stack.getOrCreateTag();
        CompoundTag tagToGet = stack.getSubTag("item" + s);
        ItemStack toGet = ItemStack.fromTag(tagToGet);
        if (toGet.getItem() != Items.AIR) {
            --stacks;
            stack.removeSubTag("item" + s);
        }
        return toGet;
    }

    public ItemStack getLastItem(ItemStack stack)
    {
        return getItemAtSlot(stack, stacks);
    }

    public ItemStack checkItemAtSlot(ItemStack stack, int s)
    {
        stack.getOrCreateTag();
        CompoundTag tagToGet = stack.getSubTag("item" + s);
        return ItemStack.fromTag(tagToGet);
    }

    public void storeItemAtSlot(ItemStack stackSource, ItemStack toStore, int s)
    {
        stackSource.getOrCreateTag();
        CompoundTag tagToStore = new CompoundTag();
        toStore.toTag(tagToStore);
        stackSource.putSubTag("item" + s, tagToStore);
        if (toStore.getItem() != Items.AIR)
            ++stacks;
    }

    public void storeItemAtLastSlot(ItemStack stackSource, ItemStack toStore)
    {
        storeItemAtSlot(stackSource, toStore, stacks);
    }

    public int getStacks()
    {
        return stacks;
    }
}
