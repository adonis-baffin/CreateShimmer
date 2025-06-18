package com.adonis.createshimmer.common.fluids.fierytear;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class FieryTearLiquidBlock extends LiquidBlock {
    public FieryTearLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.tickCount % 5 == 0 && !entity.fireImmune()) {
            entity.igniteForSeconds(3);
        }
    }
}
