package com.adonis.createshimmer.common.item.tool;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.common.registry.CSTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 微光斧 - 无限耐久，7点伤害（微光状态下9点）
 */
public class ShimmerAxeItem extends AxeItem {
    private final AbstractShimmerTool toolHelper = new AbstractShimmerTool() {};

    public ShimmerAxeItem(Properties properties) {
        super(CSTiers.SHIMMER, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        // 基础伤害为7
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                6.0f,  // 显示为7伤害 (6 + 1基础)
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

    public ItemAttributeModifiers getAttributeModifiers(ItemStack stack, net.minecraft.world.entity.EquipmentSlot slot, LivingEntity entity) {
        if (slot != net.minecraft.world.entity.EquipmentSlot.MAINHAND) {
            return ItemAttributeModifiers.EMPTY;
        }

        // 检查使用者是否有微光效果
        boolean hasShimmer = entity != null && entity.hasEffect(CSEffects.SHIMMER_EFFECT);
        float damage = hasShimmer ? 8.0f : 6.0f;  // 微光状态下+2伤害

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
                                -3.0f,
                                AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // 获取使用者，检查是否有微光效果
        float baseSpeed = super.getDestroySpeed(stack, state);

        // 如果是有效方块，应用微光加成
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            return baseSpeed;  // 会在挖掘时通过事件处理
        }

        return baseSpeed;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        return toolHelper.handleBlockMine(stack, level, state, pos, entity, 10);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return toolHelper.handleHurtEnemy(stack, target, attacker, 10, 0.3);
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