package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.util.ImplementedInventory;
import com.sun.istack.internal.NotNull;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class BasicGeneratorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, PropertyDelegateHolder, ImplementedInventory, Tickable
{
    private final EnergyTier tier;
    private double energyStored = 0;
    private double max = 2000;
    private final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public int fuelSlot = 0;
    public int burnTime;
    public int totalBurnTime = 0;
    public boolean isBurning;
    public boolean lastTickBurning;
    ItemStack burnItem;

    public BasicGeneratorBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.BASIC_GENERATOR);
        this.tier = tier;
        this.propertyDelegate = new PropertyDelegate()
        {
            @Override
            public int get(int index)
            {
                switch (index) {
                    case 0:
                        return (int) getStoredEnergy();
                    case 1:
                        return (int) getMaxEnergy();
                    default:
                        return -1;
                }
            }

            @Override
            public void set(int index, int value)
            {
                switch (index) {
                    case 0:
                        setStoredEnergy(value);
                        break;
                    case 1:
                        setMaxEnergy(value);
                        break;
                }
            }

            @Override
            public int size()
            {
                return 2;
            }
        };
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        energyStored = tag.getDouble("energyStored");
        Inventories.fromTag(tag, items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putDouble("energyStored", energyStored);
        Inventories.toTag(tag, items);
        return tag;
    }

    public static int getItemBurnTime(@NotNull ItemStack stack)
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
        if (getStoredEnergy() < getMaxEnergy()) {
            if (burnTime > 0) {
                burnTime--;
                setStoredEnergy(getStoredEnergy() + 2D);
                isBurning = true;
            }
        } else
            isBurning = false;
        if (burnTime == 0) {
            burnTime = totalBurnTime = BasicGeneratorBlockEntity.getItemBurnTime(items.get(fuelSlot));
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
        lastTickBurning = isBurning;

        if (getStoredEnergy() > 0) {
            for (Direction side : Direction.values()) {
                BasicEnergyStorageBlockEntity blockEntity = (BasicEnergyStorageBlockEntity) getWorld().getBlockEntity(getPos().offset(side));
                if (blockEntity == null)
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

    @Override
    public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new BasicGeneratorController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public PropertyDelegate getPropertyDelegate()
    {
        return propertyDelegate;
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
