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
