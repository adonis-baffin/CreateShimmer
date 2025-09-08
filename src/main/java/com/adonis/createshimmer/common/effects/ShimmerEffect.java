package com.adonis.createshimmer.common.effects;

import com.adonis.createshimmer.common.item.tool.ShimmerSwordItem;
import com.adonis.createshimmer.common.item.tool.ShimmerAxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerPickaxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerShovelItem;
import com.adonis.createshimmer.common.registry.CSEffects;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ShimmerEffect extends MobEffect {
    // 属性修改器数值
    public static final double ATTACK_SPEED_MODIFIER = 0.35;      // +35% 攻击速度
    public static final double ATTACK_DAMAGE_MODIFIER = 0;      // +0 攻击伤害
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

//    public List<ItemStack> getCurativeItems() {
//        // 返回空列表，表示不能通过牛奶等物品清除
//        return Collections.emptyList();
//    }

    public static class ShimmerEventHandler {

        // 统一的伤害配置（所有生物都一样）
        private static final float ADDITIONAL_MAGIC_DAMAGE = 8.0f;
        private static final double ADDITIONAL_DAMAGE_CHANCE = 0.4;

        /**
         * 处理微光工具的挖掘速度加成
         */
        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            ItemStack heldItem = player.getMainHandItem();

            // 检查是否持有微光工具
            if (heldItem.getItem() instanceof ShimmerPickaxeItem ||
                    heldItem.getItem() instanceof ShimmerAxeItem ||
                    heldItem.getItem() instanceof ShimmerShovelItem) {

                // 检查玩家是否有微光效果
                if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                    // 从木制速度(2.0)提升到钻石速度(8.0)，所以乘以4
                    event.setNewSpeed(event.getNewSpeed() * 4.0f);
                }
            }
        }

        /**
         * 处理微光工具在微光状态下的额外伤害
         */
        @SubscribeEvent
        public static void onLivingDamagePreForShimmerTools(LivingDamageEvent.Pre event) {
            DamageSource source = event.getSource();

            // 检查是否是玩家造成的伤害
            if (source.getEntity() instanceof Player player) {
                ItemStack weapon = player.getMainHandItem();

                // 检查玩家是否有微光效果并使用微光工具
                if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                    float extraDamage = 0;

                    if (weapon.getItem() instanceof ShimmerSwordItem) {
                        extraDamage = 3.0f;  // 剑+3伤害
                    } else if (weapon.getItem() instanceof ShimmerAxeItem) {
                        extraDamage = 2.0f;  // 斧+2伤害
                    } else if (weapon.getItem() instanceof ShimmerPickaxeItem) {
                        extraDamage = 3.0f;  // 镐+3伤害
                    } else if (weapon.getItem() instanceof ShimmerShovelItem) {
                        extraDamage = 3.0f;  // 锹+3伤害
                    }

                    if (extraDamage > 0) {
                        // 增加额外伤害
                        event.setNewDamage(event.getNewDamage() + extraDamage);
                    }
                }
            }
        }

        /**
         * 处理微光剑的横扫攻击效果
         * 这个方法会在所有伤害事件（包括横扫）触发时调用
         */
        @SubscribeEvent
        public static void onLivingDamagePreForSweep(LivingDamageEvent.Pre event) {
            DamageSource source = event.getSource();
            LivingEntity target = event.getEntity();

            // 检查攻击者是否存在并使用微光剑
            if (source.getEntity() instanceof LivingEntity attacker) {
                ItemStack weapon = attacker.getMainHandItem();

                // 检查是否使用微光剑
                if (weapon.getItem() instanceof ShimmerSwordItem) {
                    // 给被攻击的目标施加微光效果（包括横扫的目标）
                    if (!target.level().isClientSide()) {
                        // 100%给目标施加微光效果
                        target.addEffect(new MobEffectInstance(
                                CSEffects.SHIMMER_EFFECT,
                                140, // 7秒
                                0,   // 效果等级
                                false,
                                true,
                                true));

                        // 生成较少的粒子效果（避免横扫时粒子过多）
                        if (target.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(
                                    ParticleTypes.DRAGON_BREATH,
                                    target.getX(),
                                    target.getY() + target.getBbHeight() / 2,
                                    target.getZ(),
                                    3,  // 较少的粒子数量
                                    0.1, 0.1, 0.1,
                                    0.02);
                        }
                    }
                }
            }
        }

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
         * 攻击事件处理，仅用于视觉效果
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