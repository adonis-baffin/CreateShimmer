package com.adonis.createshimmer.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

/**
 * 辅助类用于处理整合模组物品的引用
 */
public class IntegrationItemWrapper implements ItemLike {
    private final ResourceLocation itemId;

    private IntegrationItemWrapper(ResourceLocation itemId) {
        this.itemId = itemId;
    }

    public static IntegrationItemWrapper of(String modId, String itemName) {
        return new IntegrationItemWrapper(ResourceLocation.fromNamespaceAndPath(modId, itemName));
    }

    @Override
    public Item asItem() {
        // 运行时获取物品，如果模组未加载则返回空气
        Item item = BuiltInRegistries.ITEM.get(itemId);
        return item != Items.AIR ? item : Items.AIR;
    }

    public ResourceLocation getId() {
        return itemId;
    }
}