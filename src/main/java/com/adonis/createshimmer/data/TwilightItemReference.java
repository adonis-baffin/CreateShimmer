package com.adonis.createshimmer.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

/**
 * 用于引用暮色森林物品的包装器
 */
public class TwilightItemReference implements ItemLike {
    private final ResourceLocation itemId;

    private TwilightItemReference(ResourceLocation itemId) {
        this.itemId = itemId;
    }

    public static TwilightItemReference of(String itemName) {
        return new TwilightItemReference(ResourceLocation.fromNamespaceAndPath("twilightforest", itemName));
    }

    @Override
    public Item asItem() {
        // 在数据生成时，尝试获取实际的物品
        // 如果不存在则返回占位符
        Item item = BuiltInRegistries.ITEM.get(itemId);
        return item != null && item != Items.AIR ? item : Items.BARRIER;
    }

    public ResourceLocation getId() {
        return itemId;
    }

    /**
     * 创建一个指向暮色森林物品的 ResourceLocation
     * 这个方法专门用于在配方中引用暮色森林的物品
     */
    public static ResourceLocation createTwilightResourceLocation(String itemName) {
        return ResourceLocation.fromNamespaceAndPath("twilightforest", itemName);
    }
}
