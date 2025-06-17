package com.adonis.createshimmer.common.fluids.shimmer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class ShimmerLiquidBlock extends LiquidBlock {
    public ShimmerLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.isAffectedByPotions() && entity.tickCount % 5 == 0) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false, false));
        }
    }
}