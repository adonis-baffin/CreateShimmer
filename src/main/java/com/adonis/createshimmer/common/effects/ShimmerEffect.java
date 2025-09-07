package com.adonis.createshimmer.common.effects;

import com.adonis.createshimmer.common.registry.CSEffects;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ShimmerEffect extends MobEffect {
    // 属性修改器数值
    public static final double ATTACK_SPEED_MODIFIER = 0.35;      // +35% 攻击速度
    public static final double ATTACK_DAMAGE_MODIFIER = 3.0;      // +3 攻击伤害
    public static final double DIG_SPEED_MODIFIER = 0.35;          // +35% 挖掘速度
    public static final double MOVEMENT_SPEED_MODIFIER = 0.35;     // +35% 移动速度

    // 饥饿消耗配置
    private static final float HUNGER_EXHAUSTION_PER_TICK = 0.005f;  // 每tick消耗的饥饿值
    private static final int HUNGER_TICK_INTERVAL = 20;              // 每20tick（1秒）消耗一次

    public ShimmerEffect() {
        super(MobEffectCategory.NEUTRAL, 0x4A148C);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // 处理饥饿值消耗
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            // 不对创造模式和观察者模式玩家生效
            if (!player.isCreative() && !player.isSpectator()) {
                FoodData foodData = player.getFoodData();
                foodData.addExhaustion(HUNGER_EXHAUSTION_PER_TICK * HUNGER_TICK_INTERVAL);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 每20tick（1秒）执行一次
        return duration % HUNGER_TICK_INTERVAL == 0;
    }

    public List<ItemStack> getCurativeItems() {
        // 返回空列表，表示不能通过牛奶等物品清除
        return Collections.emptyList();
    }

    public static class ShimmerEventHandler {

        // 统一的伤害配置（所有生物都一样）
        private static final float ADDITIONAL_MAGIC_DAMAGE = 8.0f;
        private static final double ADDITIONAL_DAMAGE_CHANCE = 0.4;

        @SubscribeEvent
        public static void onLivingDamaged(LivingDamageEvent.Post event) {
            LivingEntity entity = event.getEntity();
            DamageSource source = event.getSource();

            // 避免无限循环：如果伤害源已经是魔法伤害，直接返回
            if (source.is(net.minecraft.world.damagesource.DamageTypes.MAGIC)) {
                return;
            }

            // 检查实体是否拥有微光效果
            if (!entity.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                return;
            }

            // 只在服务端处理
            if (entity.level().isClientSide()) {
                return;
            }

            // 玩家创造模式和观察者模式跳过
            if (entity instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    return;
                }
            }

            // 检查实际造成的伤害值
            // 只有造成至少0.5伤害才触发微光额外伤害
            float actualDamage = event.getNewDamage();
            if (actualDamage < 0.5f) {
                return;  // 伤害太小或被完全格挡，不触发微光伤害
            }

            // 根据概率决定是否造成额外伤害（所有生物统一概率）
            if (entity.level().random.nextDouble() < ADDITIONAL_DAMAGE_CHANCE) {
                // 临时存储当前的无敌时间
                int originalInvulnerableTime = entity.invulnerableTime;

                // 重置无敌时间，允许立即造成伤害
                entity.invulnerableTime = 0;

                // 应用魔法伤害
                DamageSource magicDamage = entity.damageSources().magic();
                boolean damaged = entity.hurt(magicDamage, ADDITIONAL_MAGIC_DAMAGE);

                // 如果伤害没有成功应用，恢复无敌时间
                if (!damaged) {
                    entity.invulnerableTime = originalInvulnerableTime;
                }

                // 添加视觉反馈（粒子效果）
                if (entity.level() instanceof ServerLevel serverLevel) {
                    double x = entity.getX();
                    double y = entity.getY() + entity.getBbHeight() / 2.0;
                    double z = entity.getZ();

                    // 生成紫色粒子效果
                    for (int i = 0; i < 5; i++) {
                        double offsetX = entity.level().random.nextGaussian() * 0.2;
                        double offsetY = entity.level().random.nextGaussian() * 0.2;
                        double offsetZ = entity.level().random.nextGaussian() * 0.2;

                        serverLevel.sendParticles(
                                ParticleTypes.WITCH,
                                x + offsetX,
                                y + offsetY,
                                z + offsetZ,
                                1,
                                0, 0, 0,
                                0.05);
                    }
                }
            }
        }

        /**
         * 可选：攻击事件处理，仅用于视觉效果
         */
        @SubscribeEvent
        public static void onLivingAttack(LivingDamageEvent.Pre event) {
            DamageSource source = event.getSource();

            // 仅用于视觉效果，不修改伤害
            if (source.getEntity() instanceof LivingEntity attacker) {
                if (attacker.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                    // 只添加视觉效果，不修改伤害
                    if (attacker.level() instanceof ServerLevel serverLevel && event.getEntity() != null) {
                        serverLevel.sendParticles(
                                ParticleTypes.DRAGON_BREATH,
                                event.getEntity().getX(),
                                event.getEntity().getY() + event.getEntity().getBbHeight() / 2,
                                event.getEntity().getZ(),
                                3,
                                0.1, 0.1, 0.1,
                                0.02);
                    }
                }
            }
        }
    }
}
