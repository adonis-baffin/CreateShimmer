package com.adonis.createshimmer.common.effects;

import com.adonis.createshimmer.common.item.tool.ShimmerAxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerPickaxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerShovelItem;
import com.adonis.createshimmer.common.item.tool.ShimmerSwordItem;
import com.adonis.createshimmer.common.registry.CSEffects;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ShimmerEffect extends MobEffect {
    // 属性修改器数值
    public static final double ATTACK_SPEED_MODIFIER = 0.35;
    public static final double ATTACK_DAMAGE_MODIFIER = 0;
    public static final double DIG_SPEED_MODIFIER = 0.35;
    public static final double MOVEMENT_SPEED_MODIFIER = 0.35;

    // 饥饿消耗配置
    private static final float HUNGER_EXHAUSTION_PER_TICK = 0.005f;
    private static final int HUNGER_TICK_INTERVAL = 20;

    // 追踪哪些实体不应该有属性修改器
    private static final Map<LivingEntity, Boolean> NON_PLAYER_ENTITIES = new WeakHashMap<>();

    public ShimmerEffect() {
        super(MobEffectCategory.NEUTRAL, 0x4A148C);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        // 记录非玩家实体
        if (!(entity instanceof Player)) {
            NON_PLAYER_ENTITIES.put(entity, true);
            // 立即移除非玩家实体的属性修改器
            this.removeAttributeModifiers(entity.getAttributes());
        }
        super.onEffectStarted(entity, amplifier);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(attributeMap, amplifier);
    }

    @Override
    public void onMobRemoved(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        // 清理追踪记录
        NON_PLAYER_ENTITIES.remove(entity);
        super.onMobRemoved(entity, amplifier, reason);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // 每个tick检查非玩家实体是否错误地获得了属性
        if (!(entity instanceof Player) && !NON_PLAYER_ENTITIES.containsKey(entity)) {
            NON_PLAYER_ENTITIES.put(entity, true);
            this.removeAttributeModifiers(entity.getAttributes());
        }

        // 处理饥饿值消耗 - 只对玩家生效
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            if (!player.isCreative() && !player.isSpectator()) {
                FoodData foodData = player.getFoodData();
                foodData.addExhaustion(HUNGER_EXHAUSTION_PER_TICK * HUNGER_TICK_INTERVAL);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % HUNGER_TICK_INTERVAL == 0;
    }

    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    public static class ShimmerEventHandler {
        private static final float ADDITIONAL_MAGIC_DAMAGE = 8.0f;
        private static final double ADDITIONAL_DAMAGE_CHANCE = 0.4;

        /**
         * 处理微光工具的挖掘速度加成
         */
        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            try {
                Player player = event.getEntity();
                if (player == null) return;

                ItemStack heldItem = player.getMainHandItem();
                if (heldItem == null || heldItem.isEmpty()) return;

                boolean isShimmerTool = (heldItem.getItem() instanceof ShimmerPickaxeItem ||
                        heldItem.getItem() instanceof ShimmerAxeItem ||
                        heldItem.getItem() instanceof ShimmerShovelItem);

                if (!isShimmerTool) return;

                if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                    float currentSpeed = event.getNewSpeed();

                    if (currentSpeed <= 0) {
                        currentSpeed = event.getOriginalSpeed();
                    }
                    if (currentSpeed <= 0) {
                        currentSpeed = 1.0f;
                    }

                    float newSpeed = currentSpeed * 4.0f;
                    newSpeed = Math.max(0.1f, Math.min(newSpeed, 100.0f));

                    event.setNewSpeed(newSpeed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 处理微光工具在微光状态下的额外伤害
         */
        @SubscribeEvent
        public static void onLivingDamagePreForShimmerTools(LivingDamageEvent.Pre event) {
            try {
                DamageSource source = event.getSource();
                if (source == null) return;

                if (source.getEntity() instanceof Player player) {
                    ItemStack weapon = player.getMainHandItem();
                    if (weapon == null || weapon.isEmpty()) return;

                    if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                        float extraDamage = 0;

                        if (weapon.getItem() instanceof ShimmerSwordItem) {
                            extraDamage = 3.0f;
                        } else if (weapon.getItem() instanceof ShimmerAxeItem) {
                            extraDamage = 2.0f;
                        } else if (weapon.getItem() instanceof ShimmerPickaxeItem) {
                            extraDamage = 3.0f;
                        } else if (weapon.getItem() instanceof ShimmerShovelItem) {
                            extraDamage = 3.0f;
                        }

                        if (extraDamage > 0) {
                            float originalDamage = event.getNewDamage();
                            event.setNewDamage(originalDamage + extraDamage);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 处理微光剑的横扫攻击效果 - 延迟施加效果
         */
        @SubscribeEvent
        public static void onLivingDamagePreForSweep(LivingDamageEvent.Pre event) {
            try {
                DamageSource source = event.getSource();
                LivingEntity target = event.getEntity();

                if (source.getEntity() instanceof LivingEntity attacker) {
                    ItemStack weapon = attacker.getMainHandItem();

                    if (weapon.getItem() instanceof ShimmerSwordItem) {
                        if (!target.level().isClientSide()) {
                            // 延迟1 tick施加效果，避免在同一伤害事件中触发额外伤害
                            ((ServerLevel) target.level()).getServer().execute(() -> {
                                if (target.isAlive() && !target.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                                    target.addEffect(new MobEffectInstance(
                                            CSEffects.SHIMMER_EFFECT,
                                            140, // 7秒
                                            0,
                                            false,
                                            true,
                                            true));
                                }
                            });

                            // 粒子效果立即生成
                            if (target.level() instanceof ServerLevel serverLevel) {
                                serverLevel.sendParticles(
                                        ParticleTypes.DRAGON_BREATH,
                                        target.getX(),
                                        target.getY() + target.getBbHeight() / 2,
                                        target.getZ(),
                                        3,
                                        0.1, 0.1, 0.1,
                                        0.02);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SubscribeEvent
        public static void onLivingDamaged(LivingDamageEvent.Post event) {
            LivingEntity entity = event.getEntity();
            DamageSource source = event.getSource();

            // 避免无限循环
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
            float actualDamage = event.getNewDamage();
            if (actualDamage < 0.5f) {
                return;
            }

            // 根据概率决定是否造成额外伤害
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

                // 添加视觉反馈
                if (entity.level() instanceof ServerLevel serverLevel) {
                    double x = entity.getX();
                    double y = entity.getY() + entity.getBbHeight() / 2.0;
                    double z = entity.getZ();

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

            if (source.getEntity() instanceof LivingEntity attacker) {
                if (attacker.hasEffect(CSEffects.SHIMMER_EFFECT)) {
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