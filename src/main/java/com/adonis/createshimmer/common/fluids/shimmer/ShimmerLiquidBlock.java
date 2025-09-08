package com.adonis.createshimmer.common.fluids.shimmer;

import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
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
        // 只在服务端处理
        if (level.isClientSide()) {
            return;
        }

        // 检查是否为生物实体
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        // 每5 tick检查一次（性能优化）
        if (entity.tickCount % 5 != 0) {
            return;
        }

        // 移除isAffectedByPotions检查，让所有生物都能获得效果
        // 但排除一些特殊情况
        if (livingEntity.isDeadOrDying()) {
            return;
        }

        // 可选：排除某些特定的生物（如凋灵）
        if (livingEntity instanceof WitherBoss) {
            return; // 凋灵免疫
        }

        // 添加效果，增加持续时间到200 tick（10秒）
        // 并且检查现有效果，如果剩余时间少于100 tick则刷新
        MobEffectInstance currentEffect = livingEntity.getEffect(CSEffects.SHIMMER_EFFECT);
        if (currentEffect == null || currentEffect.getDuration() < 40) {
            livingEntity.addEffect(new MobEffectInstance(
                    CSEffects.SHIMMER_EFFECT,
                    80,  // 持续时间：10秒
                    0,    // 等级
                    false, // 环境效果
                    true,  // 显示粒子
                    true   // 显示图标
            ));
        }
    }
}
