package com.adonis.createshimmer.common.fluids.shimmer;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class ShimmerOpenPipeEffect implements OpenPipeEffectHandler {
    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        // 性能优化：每5 tick执行一次
        if (level.getGameTime() % 5 != 0) {
            return;
        }

        // 获取区域内的所有生物（移除isAffectedByPotions检查）
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, entity -> {
            // 基本检查
            if (entity.isDeadOrDying()) {
                return false;
            }

            // 可选：排除某些免疫的生物
            if (entity instanceof WitherBoss) {
                return false;
            }

            return true;
        });

        // 给所有符合条件的生物添加效果
        for (LivingEntity entity : entities) {
            // 检查现有效果，避免频繁覆盖
            MobEffectInstance currentEffect = entity.getEffect(CSEffects.SHIMMER_EFFECT);
            if (currentEffect == null || currentEffect.getDuration() < 100) {
                entity.addEffect(new MobEffectInstance(
                        CSEffects.SHIMMER_EFFECT,
                        200,   // 持续10秒
                        0,     // 等级0
                        false, // 不是环境效果
                        true,  // 显示粒子
                        true   // 显示图标
                ));
            }
        }
    }
}
