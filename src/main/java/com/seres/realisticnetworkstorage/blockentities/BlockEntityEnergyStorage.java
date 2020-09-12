package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.BasicEnergyStorageController;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BlockEntityEnergyStorage extends BlockEntity implements NamedScreenHandlerFactory, PropertyDelegateHolder
{
    private final EnergyTier tier;
    private final PropertyDelegate propertyDelegate;
    private double energyStored = 0;
    private double max = 10000;

    public BlockEntityEnergyStorage(EnergyTier tier)
    {
        super(RNSBlockEntities.BLOCK_ENTITY_ENERGY_STORAGE);
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

    @Override
    public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player)
    {
        return new BasicEnergyStorageController(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public PropertyDelegate getPropertyDelegate()
    {
        return propertyDelegate;
    }
}
