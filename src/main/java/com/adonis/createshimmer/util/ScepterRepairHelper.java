package com.adonis.createshimmer.util;

import com.adonis.createshimmer.config.CSConfig;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ScepterRepairHelper {

    // 支持修复的四把权杖
    private static final Set<String> REPAIRABLE_SCEPTERS = Set.of(
            "twilightforest:lifedrain_scepter",
            "twilightforest:zombie_scepter",
            "twilightforest:fortification_scepter",
            "twilightforest:twilight_scepter");

    /**
     * 检查物品是否是可修复的权杖
     */
    public static boolean isRepairableScepter(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            if (isDebugMode() && !stack.isEmpty()) {
                System.out.println("Item is not damageable: " + stack.getItem());
            }
            return false;
        }

        // 检查配置是否启用
        if (!CSConfig.repair().enableScepterRepair.get()) {
            if (isDebugMode()) {
                System.out.println("Scepter repair is disabled in config");
            }
            return false;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        boolean isRepairable = REPAIRABLE_SCEPTERS.contains(itemId.toString());

        if (isDebugMode()) {
            System.out.println("Checking item: " + itemId + ", is repairable: " + isRepairable);
        }

        return isRepairable;
    }

    /**
     * 检查权杖是否需要修复（不是满耐久）
     */
    public static boolean needsRepair(ItemStack stack) {
        boolean needs = isRepairableScepter(stack) && stack.getDamageValue() > 0;

        if (isDebugMode() && isRepairableScepter(stack)) {
            System.out.println("Scepter needs repair: " + needs +
                    ", current damage: " + stack.getDamageValue() +
                    ", max damage: " + stack.getMaxDamage());
        }

        return needs;
    }

    /**
     * 获取修复成本（根据权杖类型从配置读取）
     */
    public static int getRepairCost(ItemStack stack) {  // 添加 ItemStack 参数
        if (!isRepairableScepter(stack)) {
            return 0;  // 不是权杖，返回0
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String scepterId = itemId.toString();
        int cost;

        if (scepterId.equals("twilightforest:twilight_scepter")) {
            cost = CSConfig.repair().twilightScepterRepairCost.get();
        } else if (scepterId.equals("twilightforest:lifedrain_scepter")) {
            cost = CSConfig.repair().lifedrainScepterRepairCost.get();
        } else if (scepterId.equals("twilightforest:zombie_scepter")) {
            cost = CSConfig.repair().zombieScepterRepairCost.get();
        } else if (scepterId.equals("twilightforest:fortification_scepter")) {
            cost = CSConfig.repair().fortificationScepterRepairCost.get();
        } else {
            cost = 0;  // 未知权杖
        }

        if (isDebugMode()) {
            System.out.println("Repair cost for " + scepterId + ": " + cost + "mB");
        }
        return cost;
    }

    /**
     * 获取修复量（从配置读取，默认9点耐久）
     */
    public static int getRepairAmount() {
        int amount = CSConfig.repair().scepterRepairAmount.get();
        if (isDebugMode()) {
            System.out.println("Repair amount: " + amount + " durability points");
        }
        return amount;
    }

    /**
     * 应用修复到权杖
     */
    public static ItemStack repairScepter(ItemStack originalScepter) {
        if (!needsRepair(originalScepter)) {
            if (isDebugMode()) {
                System.out.println("Scepter doesn't need repair, returning copy");
            }
            return originalScepter.copy();
        }

        ItemStack repairedScepter = originalScepter.copy();
        int currentDamage = repairedScepter.getDamageValue();
        int repairAmount = getRepairAmount();
        int newDamage = Math.max(0, currentDamage - repairAmount);

        if (isDebugMode()) {
            System.out.println("Processing scepter repair: " + currentDamage + " -> " + newDamage +
                    " (repaired " + repairAmount + " points)");
        }

        repairedScepter.setDamageValue(newDamage);
        return repairedScepter;
    }

    /**
     * 检查是否启用调试模式
     */
    public static boolean isDebugMode() {
        try {
            return CSConfig.repair().debugMode.get();
        } catch (Exception e) {
            return false; // 如果配置未加载，默认关闭调试
        }
    }
}