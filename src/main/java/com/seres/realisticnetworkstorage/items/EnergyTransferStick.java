package com.seres.realisticnetworkstorage.items;

import com.seres.realisticnetworkstorage.blockentities.BasicEnergyStorageBlockEntity;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyStorage;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.energy.ItemEnergyStorage;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class EnergyTransferStick extends ItemEnergyStorage
{
    public EnergyTransferStick(Settings settings, double max, EnergyTier tier)
    {
        super(settings, max, tier);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        PlayerEntity player = context.getPlayer();
        assert player != null;
        World world = context.getWorld();
        ItemStack energyItemStack = player.getStackInHand(context.getHand());
        EnergyTransferStick energyItem = (EnergyTransferStick) energyItemStack.getItem();

        if (!world.isClient()) {
            PacketByteBuf beforeData = new PacketByteBuf(Unpooled.buffer());

            beforeData.writeString("BEFORE: " + energyItem.getStoredEnergy(energyItemStack));
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, beforeData);
        }
        if (!world.isClient() && world.getBlockState(context.getBlockPos()).getBlock() instanceof BasicEnergyStorage) {
            if (energyItem.getStoredEnergy(energyItemStack) > 0) {
                BasicEnergyStorageBlockEntity energyBlock = (BasicEnergyStorageBlockEntity) world.getBlockEntity(context.getBlockPos());
                assert energyBlock != null;
                double energyToTransfer = Math.min(energyItem.getMaxOutput(), energyItem.getStoredEnergy(energyItemStack));
                double energyTransferred = Math.min(energyToTransfer, energyBlock.getMaxInput());
                double test = energyBlock.getStoredEnergy() + energyTransferred;

                if (test >= energyBlock.getMaxEnergy())
                    energyTransferred -= test - energyBlock.getMaxEnergy();
                energyItem.setStoredEnergy(energyItemStack, energyItem.getStoredEnergy(energyItemStack) - energyTransferred);
                energyBlock.addEnergy(player, energyTransferred);
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
