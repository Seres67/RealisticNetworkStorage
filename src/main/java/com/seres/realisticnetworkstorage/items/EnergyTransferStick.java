/*
 * This file is part of Seres67, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 Seres67
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.seres.realisticnetworkstorage.items;

import com.seres.realisticnetworkstorage.blockentities.BasicEnergyBlockEntity;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyBlock;
import com.seres.realisticnetworkstorage.energy.BasicEnergyItem;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class EnergyTransferStick extends BasicEnergyItem
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
        if (!world.isClient() && world.getBlockState(context.getBlockPos()).getBlock() instanceof BasicEnergyBlock) {
            if (energyItem.getStoredEnergy(energyItemStack) > 0) {
                BasicEnergyBlockEntity energyBlock = (BasicEnergyBlockEntity) world.getBlockEntity(context.getBlockPos());
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
