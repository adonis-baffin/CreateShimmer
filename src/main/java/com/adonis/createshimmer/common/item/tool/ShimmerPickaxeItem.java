package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.common.registry.CSTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 微光镐 - 无限耐久，2点伤害（微光状态下5点），钻石级挖掘
 */
public class ShimmerPickaxeItem extends PickaxeItem {
    private final AbstractShimmerTool toolHelper = new AbstractShimmerTool() {};

    public ShimmerPickaxeItem(Properties properties) {
        super(CSTiers.SHIMMER, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        // 基础伤害为2（显示值保持不变）
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                1.0f,  // 显示为2伤害 (1 + 1基础)
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -2.8f,  // 镐的标准攻击速度（显示为1.2）
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public float getAttackDamage() {
        // 重写此方法以在实际战斗中提供动态伤害
        return 1.0f; // 基础显示值保持为2
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 在实际攻击时检查并应用额外伤害
        if (attacker.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            // 微光状态下造成额外3点伤害（总计5点）
            target.hurt(attacker.damageSources().mobAttack(attacker), 3.0f);
        }

        // 使用基类方法处理效果
        return toolHelper.handleHurtEnemy(stack, target, attacker, 8, 0.2);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        // 使用基类方法处理，镐的粒子数量较多
        return toolHelper.handleBlockMine(stack, level, state, pos, entity, 15);
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