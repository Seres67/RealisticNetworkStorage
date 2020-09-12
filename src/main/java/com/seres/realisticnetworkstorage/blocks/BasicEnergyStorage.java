package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.blockentities.BlockEntityEnergyStorage;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BasicEnergyStorage extends BlockWithEntity implements BlockEntityProvider
{
    private final EnergyTier tier;

    public BasicEnergyStorage(Settings settings, EnergyTier tier)
    {
        super(settings);
        this.tier = tier;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if (world.isClient)
            return ActionResult.SUCCESS;
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BlockEntityEnergyStorage)
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity)
    {
        if (!world.isClient() && entity instanceof PlayerEntity) {
            BlockEntityEnergyStorage energyBlockEntity = (BlockEntityEnergyStorage) world.getBlockEntity(pos);
            assert energyBlockEntity != null;
            CompoundTag tag = new CompoundTag();
            energyBlockEntity.toTag(tag);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView)
    {
        return new BlockEntityEnergyStorage(tier);
    }
}