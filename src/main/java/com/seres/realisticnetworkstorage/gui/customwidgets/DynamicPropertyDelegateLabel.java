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

package com.seres.realisticnetworkstorage.gui.customwidgets;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.TranslatableText;

public class DynamicPropertyDelegateLabel extends WLabel
{
    private final String text;
    private final ThreadLocal<Object[]> cache;
    private final int[] indexes;
    protected PropertyDelegate properties;

    public DynamicPropertyDelegateLabel(String text, int... indexes)
    {
        super("");
        this.text = text;
        this.cache = ThreadLocal.withInitial(() -> new Object[indexes.length]);
        this.indexes = indexes;
    }

    @Override
    @SuppressWarnings("MethodCallSideOnly")
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY)
    {
        Object[] args = this.cache.get();
        for (int i = 0; i < args.length; i++) {
            int index = this.indexes[i];
            args[i] = this.properties.get(index);
        }
        this.setText(new TranslatableText(this.text, args));
        super.paint(matrices, x, y, mouseX, mouseY);
    }

    @Override
    public void validate(GuiDescription host)
    {
        super.validate(host);
        if (this.properties == null) {
            this.properties = host.getPropertyDelegate();
        }

    }
}
