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

package com.seres.realisticnetworkstorage.gui.basicenergystorage;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.gui.RNSScreens;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class BasicEnergyStorageController extends SyncedGuiDescription
{
    public BasicEnergyStorageController(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(RNSScreens.basicEnergyStorageScreen, syncId, playerInventory, getBlockInventory(context, 0), getBlockPropertyDelegate(context, 2));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        Texture bg = new Texture(new Identifier(RealisticNetworkStorage.MODID, "textures/gui/bar.png"));
        Texture b = new Texture(new Identifier(RealisticNetworkStorage.MODID, "textures/gui/bar_mark.png"));
        WBar bar = new WBar(bg, b, 0, 1, WBar.Direction.UP);
        bar.withTooltip("gui.realisticnetworkstorage.energy_storage.tooltip");
        bar.validate(this);
        root.add(bar, 8, 1, 1, 3);
        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
