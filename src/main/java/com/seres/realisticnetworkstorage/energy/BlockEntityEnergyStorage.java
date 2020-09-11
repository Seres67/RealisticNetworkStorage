package com.seres.realisticnetworkstorage.energy;

import com.seres.realisticnetworkstorage.blockentities.RNSBlockEntities;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class BlockEntityEnergyStorage extends BlockEntity
{
    private double energyStored = 0;
    private EnergyTier tier;

    public BlockEntityEnergyStorage(EnergyTier tier)
    {
        super(RNSBlockEntities.BLOCK_ENTITY_ENERGY_STORAGE);
        this.tier = tier;
    }

    public BlockEntityEnergyStorage()
    {
        super(RNSBlockEntities.BLOCK_ENTITY_ENERGY_STORAGE);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        energyStored = tag.getDouble("energyStored");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putDouble("energyStored", energyStored);
        return tag;
    }

    public void addEnergy(PlayerEntity player, double amount)
    {
        energyStored += amount;
        markDirty();
        PacketByteBuf blockData = new PacketByteBuf(Unpooled.buffer());

        blockData.writeString("Block has: " + energyStored);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, blockData);
    }

    public double getStoredEnergy()
    {
        return energyStored;
    }

    public double getMaxOutput()
    {
        return tier.getMaxOutput();
    }

    public double getMaxInput()
    {
        return tier.getMaxInput();
    }
}
