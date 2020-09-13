package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BasicEnergyGuiBlockEntity extends BasicEnergyBlockEntity implements NamedScreenHandlerFactory, PropertyDelegateHolder
{
    private final PropertyDelegate propertyDelegate;

    public BasicEnergyGuiBlockEntity(BlockEntityType<?> type, EnergyTier tier)
    {
        super(type, tier);
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
    public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public PropertyDelegate getPropertyDelegate()
    {
        return propertyDelegate;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return null;
    }
}
