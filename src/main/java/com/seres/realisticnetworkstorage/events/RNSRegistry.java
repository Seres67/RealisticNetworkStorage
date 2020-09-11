package com.seres.realisticnetworkstorage.events;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.blockentities.RNSBlockEntities;
import com.seres.realisticnetworkstorage.blocks.BasicBlock;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyStorage;
import com.seres.realisticnetworkstorage.blocks.RNSBlocks;
import com.seres.realisticnetworkstorage.energy.BlockEntityEnergyStorage;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.items.EnergyTransferStick;
import com.seres.realisticnetworkstorage.items.RNSItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RNSRegistry
{
    public static void registerEverything()
    {
        registerBlocks();
        registerItems();
        registerBlockEntities();
    }

    private static void registerBlocks()
    {
        registerBlock(RNSBlocks.BASIC_BLOCK = new BasicBlock(AbstractBlock.Settings.of(Material.STONE)), "basic_block");
        registerBlock(RNSBlocks.BASIC_ENERGY_BLOCK = new BasicEnergyStorage(AbstractBlock.Settings.of(Material.STONE), EnergyTier.MID), "basic_energy_storage");
    }

    private static void registerItems()
    {
        registerItem(RNSItems.ENERGY_ITEM = new EnergyTransferStick(new Item.Settings().group(RealisticNetworkStorage.ITEMGROUP), 1000, EnergyTier.HIGH), "energy_transfer_stick");
    }

    private static void registerBlockEntities()
    {
        RNSBlockEntities.BLOCK_ENTITY_ENERGY_STORAGE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                "realisticnetworkstorage:basic_energy_block_entity", BlockEntityType.Builder.create(BlockEntityEnergyStorage::new, RNSBlocks.BASIC_ENERGY_BLOCK).build(null));
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
