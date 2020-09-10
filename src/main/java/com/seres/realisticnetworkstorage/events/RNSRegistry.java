package com.seres.realisticnetworkstorage.events;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.blockentities.BasicEnergyBlockEntity;
import com.seres.realisticnetworkstorage.blockentities.RNSBlockEntities;
import com.seres.realisticnetworkstorage.blocks.BasicBlock;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyBlock;
import com.seres.realisticnetworkstorage.blocks.RNSBlocks;
import com.seres.realisticnetworkstorage.items.EnergyItem;
import com.seres.realisticnetworkstorage.items.RNSItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.*;

public class RNSRegistry
{
    public static void registerEverything()
    {
        registerBlocks();
        registerItems();
        registerBlockEntities();
        registerEnergyHolders();
    }

    private static void registerBlocks()
    {
        registerBlock(RNSBlocks.BASIC_BLOCK = new BasicBlock(AbstractBlock.Settings.of(Material.STONE)), "basic_block");
        registerBlock(RNSBlocks.BASIC_ENERGY_BLOCK = new BasicEnergyBlock(AbstractBlock.Settings.of(Material.STONE)), "basic_energy_block");
    }

    private static void registerItems()
    {
        registerItem(RNSItems.ENERGY_ITEM = new EnergyItem(new Item.Settings().group(RealisticNetworkStorage.ITEMGROUP)), "energy_item");
    }

    private static void registerBlockEntities()
    {
        RNSBlockEntities.BASIC_ENERGY_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "realisticnetworkstorage:basic_energy_block_entity", BlockEntityType.Builder.create(BasicEnergyBlockEntity::new, RNSBlocks.BASIC_ENERGY_BLOCK).build(null));
    }

    private static void registerEnergyHolders()
    {
        Energy.registerHolder(object -> {
            if (object instanceof ItemStack) {
                return ((ItemStack) object).getItem() instanceof EnergyHolder;
            }
            return false;
        }, is -> {
            final EnergyItem energyHolder = (EnergyItem) ((ItemStack) is).getItem();
            return new EnergyStorage()
            {
                @Override
                public double getStored(EnergySide side)
                {
                    return energyHolder.getStoredEnergy();
                }

                @Override
                public void setStored(double amount)
                {
                    energyHolder.setStoredEnergy(amount);
                }

                @Override
                public double getMaxStoredPower()
                {
                    return energyHolder.getMaxStoredPower();
                }

                @Override
                public EnergyTier getTier()
                {
                    return energyHolder.getTier();
                }
            };
        });
    }

    public static void registerBlock(Block block, String name)
    {
        Identifier id = new Identifier("realisticnetworkstorage:" + name);
        Item.Settings itemGroup = new Item.Settings().group(RealisticNetworkStorage.ITEMGROUP);
        Registry.register(Registry.BLOCK, id, block);
        BlockItem itemBlock = new BlockItem(block, itemGroup);
        Registry.register(Registry.ITEM, id, itemBlock);
    }

    public static void registerItem(Item item, String name)
    {
        Identifier id = new Identifier("realisticnetworkstorage:" + name);

        Registry.register(Registry.ITEM, id, item);
    }
}
