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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

public class ShimmerBucketItem extends BucketItem {

    // 修正构造函数，直接接收Fluid而不是Supplier
    public ShimmerBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 如果玩家正在潜行，执行原版桶的放置逻辑
        if (player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }

        // 否则开始饮用
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
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
