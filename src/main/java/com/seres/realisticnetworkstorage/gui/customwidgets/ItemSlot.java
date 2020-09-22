package com.seres.realisticnetworkstorage.gui.customwidgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

import java.util.List;

public class ItemSlot extends WItem
{
    public ItemSlot(List<ItemStack> items)
    {
        super(items);
    }

    public ItemSlot(Tag<? extends ItemConvertible> tag)
    {
        super(tag);
    }

    public ItemSlot(ItemStack stack)
    {
        super(stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY)
    {
        super.paint(matrices, x + 1, y + 1, mouseX, mouseY);
        RenderSystem.disableDepthTest();
        ScreenDrawing.coloredRect(x + 1, y + 1, getWidth() - 2, getHeight() - 2, 0xb0_8b8b8b);
    }
}
