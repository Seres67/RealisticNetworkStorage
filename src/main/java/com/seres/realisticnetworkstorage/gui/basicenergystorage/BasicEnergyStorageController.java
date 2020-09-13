package com.seres.realisticnetworkstorage.gui.basicenergystorage;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.events.RNSRegistry;
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
        super(RNSRegistry.basicEnergyStorageScreen, syncId, playerInventory, getBlockInventory(context, 0), getBlockPropertyDelegate(context, 2));
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
