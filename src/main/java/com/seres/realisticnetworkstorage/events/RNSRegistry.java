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

package com.seres.realisticnetworkstorage.events;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.blockentities.BasicEnergyStorageBlockEntity;
import com.seres.realisticnetworkstorage.blockentities.BasicGeneratorBlockEntity;
import com.seres.realisticnetworkstorage.blockentities.DiskControllerBlockEntity;
import com.seres.realisticnetworkstorage.blockentities.RNSBlockEntities;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyStorage;
import com.seres.realisticnetworkstorage.blocks.BasicGeneratorBlock;
import com.seres.realisticnetworkstorage.blocks.DiskController;
import com.seres.realisticnetworkstorage.blocks.RNSBlocks;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.gui.RNSScreens;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageController;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerGuiController;
import com.seres.realisticnetworkstorage.items.BaseDiskItem;
import com.seres.realisticnetworkstorage.items.EnergyTransferStick;
import com.seres.realisticnetworkstorage.items.RNSItems;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class RNSRegistry
{
    public static void registerEverything()
    {
        registerBlocks();
        registerItems();
        registerBlockEntities();
        registerScreens();
    }

    private static void registerBlocks()
    {
        registerBlock(RNSBlocks.BASIC_ENERGY_STORAGE = new BasicEnergyStorage(FabricBlockSettings.of(Material.METAL), EnergyTier.MID), "basic_energy_storage");
        registerBlock(RNSBlocks.BASIC_GENERATOR = new BasicGeneratorBlock(FabricBlockSettings.of(Material.METAL), EnergyTier.LOW), "basic_generator");
        registerBlock(RNSBlocks.DISK_CONTROLLER = new DiskController(FabricBlockSettings.of(Material.METAL), EnergyTier.LOW), "disk_controller");
    }

    private static void registerItems()
    {
        Item.Settings settings = new FabricItemSettings().group(RealisticNetworkStorage.ITEMGROUP);
        registerItem(RNSItems.ENERGY_TRANSFER_STICK = new EnergyTransferStick(settings, 1000, EnergyTier.HIGH), "energy_transfer_stick");
        registerItem(RNSItems.BASE_DISK = new BaseDiskItem(settings.maxCount(1))
        {
            @Override
            public int getSize()
            {
                return 0;
            }
        }, "base_disk");
    }

    private static void registerBlockEntities()
    {
        RNSBlockEntities.BASIC_ENERGY_STORAGE = registerBlockEntity(() -> new BasicEnergyStorageBlockEntity(EnergyTier.LOW), ":basic_energy_storage_block_entity", RNSBlocks.BASIC_ENERGY_STORAGE);
        RNSBlockEntities.BASIC_GENERATOR = registerBlockEntity(() -> new BasicGeneratorBlockEntity(EnergyTier.LOW), ":basic_generator_block_entity", RNSBlocks.BASIC_GENERATOR);
        RNSBlockEntities.DISK_CONTROLLER = registerBlockEntity(() -> new DiskControllerBlockEntity(EnergyTier.LOW), ":disk_controller_block_entity", RNSBlocks.DISK_CONTROLLER);
    }

    private static void registerScreens()
    {
        RNSScreens.basicEnergyStorageScreen = registerScreen("basic_energy_storage", (id, inv) -> new BasicEnergyStorageController(id, inv, ScreenHandlerContext.EMPTY));
        RNSScreens.basicGeneratorScreen = registerScreen("basic_generator", (id, inv) -> new BasicGeneratorController(id, inv, ScreenHandlerContext.EMPTY));
        RNSScreens.diskControllerScreen = registerScreen("disk_controller", (id, inv) -> new DiskControllerGuiController(id, inv, ScreenHandlerContext.EMPTY));
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

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Supplier<T> supplier, String name, Block... blocks)
    {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, RealisticNetworkStorage.MODID + name, BlockEntityType.Builder.create(supplier, blocks).build(null));
    }

    public static <T extends SyncedGuiDescription> ScreenHandlerType<T> registerScreen(String name, ScreenHandlerRegistry.SimpleClientHandlerFactory<T> supplier)
    {
        return ScreenHandlerRegistry.registerSimple(new Identifier(RealisticNetworkStorage.MODID, name), supplier);
    }
}
