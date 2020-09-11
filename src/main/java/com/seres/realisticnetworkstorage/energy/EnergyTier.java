package com.seres.realisticnetworkstorage.energy;

public enum EnergyTier
{
    LOW("LOW", 32, 32),
    MID("MID", 64, 64),
    HIGH("HIGH", 128, 128);

    private final int maxInput;
    private final int maxOutput;

    private String name;

    EnergyTier(String name, int maxIn, int maxOut)
    {
        maxInput = maxIn;
        maxOutput = maxOut;
        this.name = name;
    }

    public int getMaxInput()
    {
        return maxInput;
    }

    public int getMaxOutput()
    {
        return maxOutput;
    }

    public String getName()
    {
        return name;
    }

    public static EnergyTier getByName(String name)
    {
        return EnergyTier.valueOf(name);
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
