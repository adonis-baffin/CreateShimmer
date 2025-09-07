package com.adonis.createshimmer.common.effects;

import com.adonis.createshimmer.common.registry.CSDamageTypes;
import com.adonis.createshimmer.common.registry.CSEffects;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
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
    public static final double ATTACK_SPEED_MODIFIER = 1.0;      // +100% 攻击速度
    public static final double ATTACK_DAMAGE_MODIFIER = 3.0;      // +3 攻击伤害
    public static final double DIG_SPEED_MODIFIER = 1.0;          // +100% 挖掘速度
    public static final double MOVEMENT_SPEED_MODIFIER = 2.0;     // +20% 移动速度

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
        private static final float ADDITIONAL_MAGIC_DAMAGE = 9.0f;
        private static final double ADDITIONAL_DAMAGE_CHANCE = 0.4;

        @SubscribeEvent
        public static void onLivingDamaged(LivingDamageEvent.Post event) {
            LivingEntity entity = event.getEntity();
            DamageSource source = event.getSource();

            // 避免无限循环：如果伤害源已经是微光魔法伤害，直接返回
            if (source.is(CSDamageTypes.SHIMMER_MAGIC)) {
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

            // 根据概率决定是否造成额外伤害（所有生物统一概率）
            if (entity.level().random.nextDouble() < ADDITIONAL_DAMAGE_CHANCE) {
                // 创建微光魔法伤害源
                DamageSource shimmerDamage = new DamageSource(
                        entity.level().registryAccess()
                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolderOrThrow(CSDamageTypes.SHIMMER_MAGIC)
                );

                // 造成额外伤害（所有生物统一伤害）
                entity.hurt(shimmerDamage, ADDITIONAL_MAGIC_DAMAGE);

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
                                0.05
                        );
                    }
                }
            }
        }

        /**
         * 可选：攻击事件处理，让有微光效果的生物造成更多伤害
         */
        @SubscribeEvent
        public static void onLivingAttack(LivingDamageEvent.Pre event) {
            DamageSource source = event.getSource();

            // 检查攻击者是否有微光效果
            if (source.getEntity() instanceof LivingEntity attacker) {
                if (attacker.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                    // 增加10%的伤害（这个会与属性修改器叠加）
                    float originalDamage = event.getOriginalDamage();
                    event.setNewDamage(originalDamage * 1.1f);

                    // 为攻击添加视觉效果
                    if (attacker.level() instanceof ServerLevel serverLevel && event.getEntity() != null) {
                        serverLevel.sendParticles(
                                ParticleTypes.DRAGON_BREATH,
                                event.getEntity().getX(),
                                event.getEntity().getY() + event.getEntity().getBbHeight() / 2,
                                event.getEntity().getZ(),
                                3,
                                0.1, 0.1, 0.1,
                                0.02
                        );
                    }
                }
            }
        }

        /**
         * 额外的挖掘消耗处理
         * 当玩家挖掘方块时增加额外的饥饿消耗
         */
        @SubscribeEvent
        public static void onBlockBreak(net.neoforged.neoforge.event.entity.player.PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            // 检查玩家是否有微光效果
            if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                // 不对创造模式玩家生效
                if (!player.isCreative() && !player.isSpectator()) {
                    // 挖掘时增加额外的饥饿消耗
                    player.getFoodData().addExhaustion(0.025f);  // 挖掘时的额外消耗
                }
            }
        }
    }
}