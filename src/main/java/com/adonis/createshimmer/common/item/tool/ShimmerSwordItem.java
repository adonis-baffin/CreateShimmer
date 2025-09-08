package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.common.registry.CSTiers;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 微光剑 - 无限耐久，4点伤害（微光状态下7点）
 */
public class ShimmerSwordItem extends SwordItem {
    private final AbstractShimmerTool toolHelper = new AbstractShimmerTool() {};

    public ShimmerSwordItem(Properties properties) {
        super(CSTiers.SHIMMER, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        // 基础伤害为4
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                3.0f,  // 显示为4伤害 (3 + 1基础)
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -2.4f,  // 剑的标准攻击速度（显示为1.6）
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public ItemAttributeModifiers getAttributeModifiers(ItemStack stack, net.minecraft.world.entity.EquipmentSlot slot, LivingEntity entity) {
        if (slot != net.minecraft.world.entity.EquipmentSlot.MAINHAND) {
            return ItemAttributeModifiers.EMPTY;
        }

        // 检查使用者是否有微光效果
        boolean hasShimmer = entity != null && entity.hasEffect(CSEffects.SHIMMER_EFFECT);
        float damage = hasShimmer ? 6.0f : 3.0f;  // 微光状态下+3伤害

        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                damage,
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -2.4f,
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // 剑对蜘蛛网有特殊效果，需要特殊处理
        if (state.is(BlockTags.SWORD_EFFICIENT)) {
            return 15.0f;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 使用基类方法处理效果，剑的粒子效果较多
        toolHelper.handleHurtEnemy(stack, target, attacker, 10, 0.3);
        return true; // 返回true表示工具正常使用
    }

    // 耐久相关方法保持不变...
    @Override
    public void setDamage(ItemStack stack, int damage) {
        AbstractShimmerTool.DurabilityMethods.setDamage(stack, damage);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return AbstractShimmerTool.DurabilityMethods.isDamageable(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return AbstractShimmerTool.DurabilityMethods.isEnchantable(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return AbstractShimmerTool.DurabilityMethods.getBarWidth(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return AbstractShimmerTool.DurabilityMethods.isBarVisible(stack);
    }
}
