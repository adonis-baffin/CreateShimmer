package com.adonis.createshimmer.common.item;

import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ShimmerBucketItem extends BucketItem {
    public ShimmerBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 获取玩家视线追踪结果
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        // 方案1：优先放置逻辑
        // 如果瞄准了方块，先尝试放置
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            // 尝试执行原版桶的放置逻辑
            InteractionResultHolder<ItemStack> placeResult = super.use(level, player, hand);

            // 如果放置成功（消耗了物品），直接返回
            if (placeResult.getResult().consumesAction()) {
                return placeResult;
            }
        }

        // 如果没有瞄准方块，或者放置失败，则开始饮用
        // 可选：如果想要必须潜行才能饮用，可以加上这个条件
        // if (player.isShiftKeyDown()) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
        // }

        // return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            // 触发成就
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            // 增加统计
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            // 给予5分钟微光效果（6000 ticks = 5分钟）
            entity.addEffect(new MobEffectInstance(CSEffects.SHIMMER_EFFECT, 6000, 0));
        }

        // 如果是玩家且不是创造模式
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            // 返回空桶
            return new ItemStack(Items.BUCKET);
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        // 饮用时间稍长，因为是一整桶
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public boolean isEdible() {
        return true;
    }
}
