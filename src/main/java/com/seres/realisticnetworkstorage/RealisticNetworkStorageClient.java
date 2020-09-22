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

package com.seres.realisticnetworkstorage;

import com.seres.realisticnetworkstorage.gui.RNSScreens;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageController;
import com.seres.realisticnetworkstorage.gui.basicenergystorage.BasicEnergyStorageScreen;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorController;
import com.seres.realisticnetworkstorage.gui.basicgenerator.BasicGeneratorScreen;
import com.seres.realisticnetworkstorage.gui.diskaccessor.DiskAccessorController;
import com.seres.realisticnetworkstorage.gui.diskaccessor.DiskAccessorScreen;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerGuiController;
import com.seres.realisticnetworkstorage.gui.diskcontroller.DiskControllerScreen;
import com.seres.realisticnetworkstorage.network.ClientboundPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class RealisticNetworkStorageClient implements ClientModInitializer
{
    @SuppressWarnings("RedundantTypeArguments")
    @Override
    public void onInitializeClient()
    {
        ClientboundPackets.init();
        ScreenRegistry.<BasicEnergyStorageController, BasicEnergyStorageScreen>register
                (RNSScreens.basicEnergyStorageScreen, (desc, inventory, title) -> new BasicEnergyStorageScreen(desc, inventory.player, title));
        ScreenRegistry.<BasicGeneratorController, BasicGeneratorScreen>register
                (RNSScreens.basicGeneratorScreen, (desc, inventory, title) -> new BasicGeneratorScreen(desc, inventory.player, title));
        ScreenRegistry.<DiskControllerGuiController, DiskControllerScreen>register
                (RNSScreens.diskControllerScreen, (desc, inventory, title) -> new DiskControllerScreen(desc, inventory.player, title));
        ScreenRegistry.<DiskAccessorController, DiskAccessorScreen>register
                (RNSScreens.diskAccessorScreen, (desc, inventory, title) -> new DiskAccessorScreen(desc, inventory.player, title));
    }
}