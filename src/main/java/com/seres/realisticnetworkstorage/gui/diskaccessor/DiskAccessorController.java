package com.seres.realisticnetworkstorage.gui.diskaccessor;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import com.seres.realisticnetworkstorage.blockentities.DiskControllerBlockEntity;
import com.seres.realisticnetworkstorage.gui.RNSScreens;
import com.seres.realisticnetworkstorage.items.disks.BaseDiskItem;
import com.seres.realisticnetworkstorage.util.ImplementedInventory;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

//TODO: More slots, limit how much stacks based on disks, better sorting algorithm (this one sucks) and check if phantom ItemStack still appear
public class DiskAccessorController extends SyncedGuiDescription
{
    BlockPos accessorPosition = null;

    public DiskAccessorController(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(RNSScreens.diskAccessorScreen, syncId, playerInventory, getBlockInventory(context, 36), getBlockPropertyDelegate(context, 2));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        Texture bg = new Texture(new Identifier(RealisticNetworkStorage.MODID, "textures/gui/bar.png"));
        Texture b = new Texture(new Identifier(RealisticNetworkStorage.MODID, "textures/gui/bar_mark.png"));
        WBar bar = new WBar(bg, b, 0, 1, WBar.Direction.UP);
        bar.withTooltip("gui.realisticnetworkstorage.energy_storage.tooltip");
        root.add(bar, 9, 2, 1, 3);
        root.add(this.createPlayerInventoryPanel(), 0, 5);
        ImplementedInventory inv = new ImplementedInventory()
        {
            final DefaultedList<ItemStack> items = DefaultedList.ofSize(36, ItemStack.EMPTY);

            @Override
            public DefaultedList<ItemStack> getItems()
            {
                return items;
            }
        };
        ArrayList<DiskControllerBlockEntity> neighbors = new ArrayList<>();

        context.run((world, position) -> {
            accessorPosition = position;
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = position.offset(dir);
                BlockEntity entity = world.getBlockEntity(neighbor);
                if (entity instanceof DiskControllerBlockEntity)
                    neighbors.add((DiskControllerBlockEntity) entity);
            }
            for (DiskControllerBlockEntity entity : neighbors) {
                for (int i = 0; i < 4; ++i) {
                    ItemStack stack = entity.getStack(i);
                    if (stack.getItem() instanceof BaseDiskItem) {
                        BaseDiskItem disk = (BaseDiskItem) stack.getItem();
                        int slot = 0;
                        for (int j = 0; j < 36; ++j) {
                            ItemStack toStore = disk.getItemAtSlot(stack, j);
                            int temp = checkIfItemStackExists(inv, toStore);
                            if (temp != -1) {
                                ItemStack tempS = inv.getStack(temp);
                                if (tempS.getMaxCount() < tempS.getCount() + toStore.getCount()) {
                                    int count = tempS.getCount() + toStore.getCount() - tempS.getMaxCount();
                                    tempS.setCount(tempS.getMaxCount());
                                    inv.setStack(temp, tempS);
                                    if (count != 0) {
                                        toStore.setCount(count);
                                        inv.setStack(slot++, toStore);
                                    }
                                } else {
                                    tempS.setCount(tempS.getCount() + toStore.getCount());
                                    inv.setStack(temp, tempS);
                                }
                            } else
                                inv.setStack(slot++, toStore);
                        }
                    }
                }
            }
        });
        WItemSlot slot = WItemSlot.of(inv, 0, 9, 4);
        root.add(slot, 0, 1);
        root.validate(this);
    }

    @Override
    public void close(PlayerEntity player)
    {
        super.close(player);
        if (accessorPosition == null) return;
        ArrayList<DiskControllerBlockEntity> neighbors = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = accessorPosition.offset(dir);
            BlockEntity entity = world.getBlockEntity(neighbor);
            if (entity instanceof DiskControllerBlockEntity)
                neighbors.add((DiskControllerBlockEntity) entity);
        }
        for (DiskControllerBlockEntity entity : neighbors) {
            for (int i = 0; i < 4; ++i) {
                ItemStack stack = entity.getStack(i);
                if (stack.getItem() instanceof BaseDiskItem) {
                    BaseDiskItem disk = (BaseDiskItem) stack.getItem();
                    int slot = 0;
                    for (int j = 36; j < 72; ++j) {
                        ItemStack toStore = getSlot(j).getStack();
                        int temp = checkIfItemStackExists(stack, toStore);
                        if (temp != -1) {
                            ItemStack tempS = disk.getItemAtSlot(stack, temp);
                            if (tempS.getMaxCount() < tempS.getCount() + toStore.getCount()) {
                                int count = tempS.getCount() + toStore.getCount() - tempS.getMaxCount();
                                tempS.setCount(tempS.getMaxCount());
                                disk.storeItemAtSlot(stack, tempS, temp);
                                if (count != 0) {
                                    toStore.setCount(count);
                                    disk.storeItemAtSlot(stack, toStore, slot++);
                                }
                            } else {
                                tempS.setCount(tempS.getCount() + toStore.getCount());
                                disk.storeItemAtSlot(stack, tempS, temp);
                            }
                        } else
                            disk.storeItemAtSlot(stack, toStore, slot++);
                    }
                }
            }
        }
    }

    private int checkIfItemStackExists(ImplementedInventory inv, ItemStack toStore)
    {
        if (toStore.getItem() == Items.AIR)
            return -1;
        for (int i = 0; i < inv.getItems().size(); ++i) {
            if (inv.getStack(i).getItem() == toStore.getItem())
                return i;
        }
        return -1;
    }

    private int checkIfItemStackExists(ItemStack disk, ItemStack toStore)
    {
        if (toStore.getItem() == Items.AIR)
            return -1;
        BaseDiskItem diskItem = (BaseDiskItem) disk.getItem();
        for (int i = 0; i < diskItem.getStacks(); ++i) {
            if (diskItem.checkItemAtSlot(disk, i).getItem() == toStore.getItem())
                return i;
        }
        return -1;
    }
}
