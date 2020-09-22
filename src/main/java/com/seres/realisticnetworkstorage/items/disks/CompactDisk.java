package com.seres.realisticnetworkstorage.items.disks;

public class CompactDisk extends BaseDiskItem
{
    public CompactDisk(Settings settings)
    {
        super(settings);
    }

    @Override
    public int getSize()
    {
        return 128;
    }
}
