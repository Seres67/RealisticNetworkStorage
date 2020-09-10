package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.blockentities.BasicEnergyBlockEntity;
import com.seres.realisticnetworkstorage.network.ServerboundPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BasicEnergyBlock extends Block implements BlockEntityProvider
{
    public BasicEnergyBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity)
    {
        if (!world.isClient() && entity instanceof PlayerEntity) {
            BasicEnergyBlockEntity energyBlockEntity = (BasicEnergyBlockEntity) world.getBlockEntity(pos);
            assert energyBlockEntity != null;
            CompoundTag tag = new CompoundTag();
            energyBlockEntity.toTag(tag);
            double energy = tag.getDouble("energyStored");
            PacketByteBuf blockData = new PacketByteBuf(Unpooled.buffer());
            blockData.writeString("Block has: " + energy);
            ServerSidePacketRegistry.INSTANCE.sendToPlayer((PlayerEntity) entity, ServerboundPackets.CHAT_MESSAGE_PACKET_ID, blockData);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView)
    {
        return new BasicEnergyBlockEntity();
    }
}