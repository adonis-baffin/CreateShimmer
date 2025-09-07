package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 微光斧 - 无限耐久，6点伤害，铁级挖掘
 */
public class ShimmerAxeItem extends AxeItem {
    private final AbstractShimmerTool toolHelper = new AbstractShimmerTool() {};

    public ShimmerAxeItem(Properties properties) {
        super(CSTiers.SHIMMER, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        // 确保伤害为6
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                5.0f,  // 显示为6伤害 (5 + 1基础)
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -3.0f,  // 斧的标准攻击速度（显示为1.0）
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        // 使用基类方法处理，斧的粒子数量中等
        return toolHelper.handleBlockMine(stack, level, state, pos, entity, 10);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 使用基类方法处理效果
        return toolHelper.handleHurtEnemy(stack, target, attacker, 10, 0.3);
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
