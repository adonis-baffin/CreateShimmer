package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

/**
 * 微光工具的抽象基类
 * 包含所有微光工具的共同行为和属性
 */
public abstract class AbstractShimmerTool {

    // ========== 配置常量 ==========
    // 效果配置
    protected static final int EFFECT_DURATION = 140;  // 7秒 (20 ticks/秒)
    protected static final float EFFECT_CHANCE = 0.4f; // 40%概率
    protected static final int EFFECT_AMPLIFIER = 0;   // 效果等级

    // 粒子效果配置
    protected static final DustParticleOptions PURPLE_DUST = new DustParticleOptions(
            new Vector3f(154f / 255f, 77f / 255f, 255f / 255f), // 0x9A4DFF 紫色
            1.0f);

    // ========== 核心方法 ==========

    /**
     * 处理微光效果的应用
     *
     * @param entity 要应用效果的实体
     * @param level  世界
     * @return 是否成功应用了效果
     */
    protected boolean applyShimmerEffect(LivingEntity entity, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        // 移除概率检查，100%触发
        // if (level.random.nextFloat() >= EFFECT_CHANCE) {
        //     return false;
        // }

        // 应用或刷新微光效果
        entity.addEffect(new MobEffectInstance(
                CSEffects.SHIMMER_EFFECT,
                EFFECT_DURATION,
                EFFECT_AMPLIFIER,
                false,  // 不是环境效果
                true,   // 显示粒子
                true    // 显示图标
        ));

        return true;
    }

    /**
     * 在方块位置生成挖掘粒子效果
     * 
     * @param level         世界
     * @param pos           方块位置
     * @param particleCount 粒子数量
     */
    protected void spawnBlockBreakParticles(Level level, BlockPos pos, int particleCount) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        for (int i = 0; i < particleCount; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.6;
            double y = pos.getY() + 0.5 + (level.random.nextDouble() - 0.5) * 0.6;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.6;

            serverLevel.sendParticles(
                    PURPLE_DUST,
                    x, y, z,
                    1,
                    0, 0, 0,
                    0);
        }
    }

    /**
     * 在实体位置生成攻击粒子效果
     * 
     * @param level         世界
     * @param target        目标实体
     * @param particleCount 粒子数量
     * @param spread        粒子扩散范围
     */
    protected void spawnAttackParticles(Level level, LivingEntity target, int particleCount, double spread) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                PURPLE_DUST,
                target.getX(),
                target.getY() + target.getBbHeight() / 2,
                target.getZ(),
                particleCount,
                spread, spread, spread,
                0);
    }

    /**
     * 处理方块挖掘时的效果
     * 
     * @param stack         物品堆
     * @param level         世界
     * @param state         方块状态
     * @param pos           方块位置
     * @param entity        使用者
     * @param particleCount 粒子数量
     * @return 是否允许破坏方块
     */
    protected boolean handleBlockMine(ItemStack stack, Level level, BlockState state, BlockPos pos,
            LivingEntity entity, int particleCount) {
        if (!level.isClientSide()) {
            // 应用微光效果
            boolean effectApplied = applyShimmerEffect(entity, level);

            // 生成粒子效果（可选：仅在效果触发时生成）
            if (effectApplied) {
                spawnBlockBreakParticles(level, pos, particleCount);
            }
        }

        return true; // 允许破坏方块
    }

    /**
     * 处理攻击敌人时的效果
     * 
     * @param stack          物品堆
     * @param target         目标
     * @param attacker       攻击者
     * @param particleCount  粒子数量
     * @param particleSpread 粒子扩散范围
     * @return 是否消耗耐久（始终返回false）
     */
    protected boolean handleHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker,
            int particleCount, double particleSpread) {
        if (!attacker.level().isClientSide()) {
            // 给攻击者应用效果
            boolean attackerEffected = applyShimmerEffect(attacker, attacker.level());

            // 给目标应用效果（独立判定）
            boolean targetEffected = applyShimmerEffect(target, target.level());

            // 如果任一效果触发，生成粒子
            if (attackerEffected || targetEffected) {
                spawnAttackParticles(attacker.level(), target, particleCount, particleSpread);
            }
        }

        return false; // 不消耗耐久
    }

    // ========== 通用工具方法 ==========

    /**
     * 标准的耐久相关方法实现
     */
    public static class DurabilityMethods {
        public static void setDamage(ItemStack stack, int damage) {
            // 不设置损伤
        }

        public static boolean isDamageable(ItemStack stack) {
            return false; // 不可损坏
        }

        public static boolean isEnchantable(ItemStack stack) {
            return true; // 可附魔
        }

        public static int getBarWidth(ItemStack stack) {
            return 13; // 始终显示满耐久条（如果显示的话）
        }

        public static boolean isBarVisible(ItemStack stack) {
            return false; // 不显示耐久条
        }
    }
}
