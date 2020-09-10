package com.seres.realisticnetworkstorage.items;

import com.seres.realisticnetworkstorage.blockentities.BasicEnergyBlockEntity;
import com.seres.realisticnetworkstorage.blocks.BasicEnergyBlock;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

/**
 * TODO refactor to use CompoundTag
 *
 * @see BasicEnergyBlockEntity
 */
public class EnergyItem extends Item implements EnergyHolder
{
    private final double max = 1000;
    private double storedEnergy = 0;

    public EnergyItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public double getMaxStoredPower()
    {
        return max;
    }

    @Override
    public EnergyTier getTier()
    {
        return EnergyTier.LOW;
    }

    public double getStoredEnergy()
    {
        return storedEnergy;
    }

    public void setStoredEnergy(double amount)
    {
        storedEnergy = amount;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        assert player != null;

        if (!world.isClient()) {
            PacketByteBuf beforeData = new PacketByteBuf(Unpooled.buffer());

            beforeData.writeString("BEFORE: " + Energy.of(player.getStackInHand(context.getHand())).getEnergy());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, beforeData);
        }
        if (!world.isClient() && world.getBlockState(context.getBlockPos()).getBlock() instanceof BasicEnergyBlock) {
            if (Energy.of(player.getStackInHand(context.getHand())).getEnergy() >= 32D) {
                BasicEnergyBlockEntity energyBlock = (BasicEnergyBlockEntity) world.getBlockEntity(context.getBlockPos());
                assert energyBlock != null;
                Energy.of(player.getStackInHand(context.getHand())).use(32D);
                energyBlock.addEnergy(player, 32D);
            }
        } else if (!world.isClient())
            Energy.of(player.getStackInHand(context.getHand())).insert(32);
        if (!world.isClient()) {
            PacketByteBuf afterData = new PacketByteBuf(Unpooled.buffer());

            afterData.writeString("AFTER: " + Energy.of(player.getStackInHand(context.getHand())).getEnergy());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, afterData);
        }
        return ActionResult.SUCCESS;
    }
}
