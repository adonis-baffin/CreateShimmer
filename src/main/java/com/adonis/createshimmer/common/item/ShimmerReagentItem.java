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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ShimmerReagentItem extends Item {
    public ShimmerReagentItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // 开始使用物品（饮用动画）
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        if (entity instanceof ServerPlayer serverPlayer) {
            // 触发饮用药水的成就
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            // 增加使用统计
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            // 消耗物品
            stack.shrink(1);
        }

        if (!level.isClientSide) {
            // 给予15秒微光效果
            entity.addEffect(new MobEffectInstance(CSEffects.SHIMMER_EFFECT, 300, 0)); // 300 ticks = 15秒
        }

        // 如果是玩家且不是创造模式，给予空瓶
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            } else {
                // 如果物品栏还有空间，直接给予空瓶
                if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                    // 如果物品栏满了，掉落空瓶
                    player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                }
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 16;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // 使用饮用动画
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        // 饮用音效
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        // 同样使用饮用音效
        return SoundEvents.GENERIC_DRINK;
    }
}
