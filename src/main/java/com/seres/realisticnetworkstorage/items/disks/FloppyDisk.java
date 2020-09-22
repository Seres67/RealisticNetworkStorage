package com.seres.realisticnetworkstorage.items.disks;

public class FloppyDisk extends BaseDiskItem
{
    public FloppyDisk(Settings settings)
    {
        super(settings);
    }

    @Override
    public int getSize()
    {
        return 64;
    }
}
