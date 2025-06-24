package com.adonis.createshimmer.common.effects;

import com.adonis.createshimmer.common.registry.CSDamageTypes;
import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Collections;
import java.util.List;

public class ShimmerEffect extends MobEffect {

    public static final double MOVEMENT_SPEED_MODIFIER = 0.2;
    public static final double BLOCK_BREAK_SPEED_MODIFIER = 0.2;
    public static final double ATTACK_SPEED_MODIFIER = 0.2;
    public static final double ATTACK_DAMAGE_MODIFIER = 2.0;

    public ShimmerEffect() {
        super(MobEffectCategory.NEUTRAL, 0x4A148C);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    public static class ShimmerEventHandler {
        // 定义额外魔法伤害的值
        private static final float ADDITIONAL_MAGIC_DAMAGE = 5.0f;
        // 【新增】定义触发额外伤害的概率，0.3 代表 30%
        private static final double ADDITIONAL_DAMAGE_CHANCE = 0.3;

        @SubscribeEvent
        public static void onPlayerDamaged(LivingDamageEvent.Post event) {
            LivingEntity entity = event.getEntity();
            DamageSource source = event.getSource();

            // 1. 拦截我们自己造成的伤害，防止无限递归
            if (source.is(CSDamageTypes.SHIMMER_MAGIC)) {
                return;
            }

            // 确保实体是玩家，并且不是创造或观察者模式
            if (!(entity instanceof Player player) || player.isCreative() || player.isSpectator()) {
                return;
            }

            // 2. 检查玩家是否拥有微光效果
            if (player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
                // 【修改】加入概率判断
                // player.level().random.nextDouble() 会生成一个 0.0 到 1.0 之间的随机双精度浮点数
                if (player.level().random.nextDouble() < ADDITIONAL_DAMAGE_CHANCE) {
                    // 3. 如果概率命中，则造成自定义伤害
                    DamageSource customSource = new DamageSource(
                            player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CSDamageTypes.SHIMMER_MAGIC)
                    );
                    player.hurt(customSource, ADDITIONAL_MAGIC_DAMAGE);
                }
            }
        }
    }
}