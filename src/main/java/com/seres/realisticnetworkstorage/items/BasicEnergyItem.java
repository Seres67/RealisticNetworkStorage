package com.seres.realisticnetworkstorage.items;

import com.seres.realisticnetworkstorage.blockentities.BasicEnergyBlockEntity;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyBlock;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class BasicEnergyItem extends Item
{
    private EnergyTier tier;
    private double max;

    public BasicEnergyItem(Settings settings, double maxEnergy, EnergyTier tier)
    {
        super(settings);
        this.tier = tier;
        this.max = maxEnergy;
    }

    public void setMaxEnergy(double max)
    {
        this.max = max;
    }

    public double getMaxEnergy()
    {
        return max;
    }

    public void setTier(EnergyTier tier)
    {
        this.tier = tier;
    }

    public EnergyTier getTier()
    {
        return tier;
    }

    public double getStoredEnergy(ItemStack stack)
    {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getDouble("energyStored");
    }

    public void setStoredEnergy(ItemStack stack, double amount)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putDouble("energyStored", amount);
        stack.setTag(tag);
    }

    public double getMaxOutput()
    {
        return getTier().getMaxOutput();
    }

    public double getMaxInput()
    {
        return getTier().getMaxInput();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        PlayerEntity player = context.getPlayer();
        assert player != null;
        World world = context.getWorld();
        ItemStack energyItemStack = player.getStackInHand(context.getHand());
        BasicEnergyItem energyItem = (BasicEnergyItem) energyItemStack.getItem();

        if (!world.isClient()) {
            PacketByteBuf beforeData = new PacketByteBuf(Unpooled.buffer());

            beforeData.writeString("BEFORE: " + energyItem.getStoredEnergy(energyItemStack));
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, beforeData);
        }
        if (!world.isClient() && world.getBlockState(context.getBlockPos()).getBlock() instanceof BasicEnergyBlock) {
            if (energyItem.getStoredEnergy(energyItemStack) > 0) {
                BasicEnergyBlockEntity energyBlock = (BasicEnergyBlockEntity) world.getBlockEntity(context.getBlockPos());
                assert energyBlock != null;
                double energyToTransfer = Math.min(energyItem.getMaxOutput(), energyItem.getStoredEnergy(energyItemStack));
                double energyTransfered = Math.min(energyToTransfer, energyBlock.getMaxInput());
                PacketByteBuf beforeData = new PacketByteBuf(Unpooled.buffer());

                beforeData.writeString("transfered: " + energyTransfered);
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, beforeData);

                energyItem.setStoredEnergy(energyItemStack, energyItem.getStoredEnergy(energyItemStack) - energyTransfered);
                energyBlock.addEnergy(player, energyTransfered);
            }
        } else if (!world.isClient()) {
            double storedEnergy = energyItem.getStoredEnergy(energyItemStack);
            double maxIn = energyItem.getMaxInput();
            energyItem.setStoredEnergy(energyItemStack, storedEnergy + maxIn);
        }
        if (!world.isClient()) {
            PacketByteBuf afterData = new PacketByteBuf(Unpooled.buffer());

            afterData.writeString("AFTER: " + energyItem.getStoredEnergy(energyItemStack));
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, afterData);
        }
        return ActionResult.SUCCESS;
    }
}
