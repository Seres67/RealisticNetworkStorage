package com.seres.realisticnetworkstorage.blocks;

import com.seres.realisticnetworkstorage.blockentities.BasicGeneratorBlockEntity;
import com.seres.realisticnetworkstorage.energy.EnergyTier;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BasicGeneratorBlock extends BlockWithEntity implements BlockEntityProvider
{
    private final EnergyTier tier;

    public BasicGeneratorBlock(Settings settings, EnergyTier tier)
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
        if (be instanceof BasicGeneratorBlockEntity)
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new BasicGeneratorBlockEntity(tier);
    }

}
