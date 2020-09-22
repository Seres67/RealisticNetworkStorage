package com.seres.realisticnetworkstorage.gui;

import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageController;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.gui.diskaccessor.DiskAccessorController;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerGuiController;
import net.minecraft.screen.ScreenHandlerType;

public class RNSScreens
{
    public static ScreenHandlerType<BasicEnergyStorageController> basicEnergyStorageScreen;
    public static ScreenHandlerType<BasicGeneratorController> basicGeneratorScreen;
    public static ScreenHandlerType<DiskControllerGuiController> diskControllerScreen;
    public static ScreenHandlerType<DiskAccessorController> diskAccessorScreen;
}
