package com.seres.realisticnetworkstorage;

import com.seres.realisticnetworkstorage.client.ModConfig;
import com.seres.realisticnetworkstorage.events.RNSRegistry;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class RealisticNetworkStorage implements ModInitializer
{
    public static final String MODID = "realisticnetworkstorage";
    public static ModConfig config;
    public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(
            new Identifier("realisticnetworkstorage", "item_group"),
            () -> new ItemStack(Items.STICK));

    @Override
    public void onInitialize()
    {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        RNSRegistry.registerEverything();
    }
}
