package com.seres.realisticnetworkstorage.blockentities;

import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class BasicEnergyBlockEntity extends BlockEntity
{
    private double energyStored = 0;
    private EnergyTier tier;

    public BasicEnergyBlockEntity(EnergyTier tier)
    {
        super(RNSBlockEntities.BASIC_ENERGY_BLOCK_ENTITY);
        this.tier = tier;
    }

    public BasicEnergyBlockEntity()
    {
        super(RNSBlockEntities.BASIC_ENERGY_BLOCK_ENTITY);
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
