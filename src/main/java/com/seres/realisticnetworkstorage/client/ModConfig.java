package com.seres.realisticnetworkstorage.client;

import com.seres.realisticnetworkstorage.RealisticNetworkStorage;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = RealisticNetworkStorage.MODID)
public class ModConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip
    public boolean enabled = true;

    public static ModConfig getInstance()
    {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
