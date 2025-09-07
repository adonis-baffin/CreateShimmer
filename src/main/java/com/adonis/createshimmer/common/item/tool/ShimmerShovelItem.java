package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 微光锹 - 无限耐久，2.5点伤害，铁级挖掘
 */
public class ShimmerShovelItem extends ShovelItem {
    private final AbstractShimmerTool toolHelper = new AbstractShimmerTool() {};

    public ShimmerShovelItem(Properties properties) {
        super(CSTiers.SHIMMER, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        // 确保伤害为2.5
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                1.5f,  // 显示为2.5伤害 (1.5 + 1基础)
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -3.0f,  // 锹的标准攻击速度（显示为1.0）
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        // 使用基类方法处理，锹的粒子数量较少
        return toolHelper.handleBlockMine(stack, level, state, pos, entity, 8);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 使用基类方法处理效果，锹的攻击粒子较少
        return toolHelper.handleHurtEnemy(stack, target, attacker, 6, 0.2);
    }

    // 耐久相关方法
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
