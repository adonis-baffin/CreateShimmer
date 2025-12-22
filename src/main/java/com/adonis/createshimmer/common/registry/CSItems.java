package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.item.*;
import com.adonis.createshimmer.common.item.tool.ShimmerAxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerPickaxeItem;
import com.adonis.createshimmer.common.item.tool.ShimmerShovelItem;
import com.adonis.createshimmer.common.item.tool.ShimmerSwordItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;

public class CSItems {
    // 原有物品
    public static final ItemEntry<ShimmerReagentItem> SHIMMER_REAGENT = REGISTRATE
            .item("shimmer_reagent", ShimmerReagentItem::new)
            .lang("Shimmer Reagent")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .register();

    public static final ItemEntry<Item> THORN_FLOUR = REGISTRATE
            .item("thorn_flour", Item::new)
            .lang("Thorn Flour")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON))
            .register();

    public static final ItemEntry<Item> RAW_KNIGHTMETAL = REGISTRATE
            .item("raw_knightmetal", Item::new)
            .lang("Raw Knightmetal")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON))
            .register();

    public static final ItemEntry<Item> CHARM_OF_SHIMMER_1 = REGISTRATE
            .item("charm_of_shimmer_1", Item::new)
            .lang("Charm of Shimmer I")
            .properties(prop -> prop
                    .rarity(Rarity.UNCOMMON))
            .register();

    public static final ItemEntry<Item> CHARM_OF_SHIMMER_2 = REGISTRATE
            .item("charm_of_shimmer_2", Item::new)
            .lang("Charm of Shimmer II")
            .properties(prop -> prop
                    .rarity(Rarity.UNCOMMON))
            .register();

    public static final ItemEntry<Item> CHARM_OF_SHIMMER_BASE = REGISTRATE
            .item("charm_of_shimmer_base", Item::new)
            .lang("Charm of Shimmer Base")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON))
            .register();

    public static final ItemEntry<Item> UNCRAFTING_TEMPLATE = REGISTRATE
            .item("uncrafting_template", Item::new)
            .lang("Uncrafting Template")
            .properties(prop -> prop
                    .rarity(Rarity.UNCOMMON))
            .register();

    public static final ItemEntry<ShimmerSwordItem> SHIMMER_SWORD = REGISTRATE
            .item("shimmer_sword", ShimmerSwordItem::new)
            .lang("Shimmer Sword")
            .properties(prop -> prop
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .stacksTo(1)
                    .fireResistant())
            .tag(net.minecraft.tags.ItemTags.SWORDS)
            .register();

    public static final ItemEntry<ShimmerAxeItem> SHIMMER_AXE = REGISTRATE
            .item("shimmer_axe", ShimmerAxeItem::new)
            .lang("Shimmer Axe")
            .properties(prop -> prop
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .stacksTo(1)
                    .fireResistant())
            .tag(net.minecraft.tags.ItemTags.AXES)
            .register();

    public static final ItemEntry<ShimmerPickaxeItem> SHIMMER_PICKAXE = REGISTRATE
            .item("shimmer_pickaxe", ShimmerPickaxeItem::new)
            .lang("Shimmer Pickaxe")
            .properties(prop -> prop
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .stacksTo(1)
                    .fireResistant())
            .tag(net.minecraft.tags.ItemTags.PICKAXES)
            .register();

    public static final ItemEntry<ShimmerShovelItem> SHIMMER_SHOVEL = REGISTRATE
            .item("shimmer_shovel", ShimmerShovelItem::new)
            .lang("Shimmer Shovel")
            .properties(prop -> prop
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .stacksTo(1)
                    .fireResistant())
            .tag(net.minecraft.tags.ItemTags.SHOVELS)
            .register();

    public static final ItemEntry<Item> SHIMMER_NUGGET = REGISTRATE
            .item("shimmer_nugget", Item::new)
            .lang("Shimmer Nugget")
            .properties(prop -> prop
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .tag(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "nuggets")))
            .register();

    // 原有桶物品
    public static final DeferredItem<BucketItem> SHIMMER_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("shimmer_bucket"));

    public static final DeferredItem<BucketItem> CARMINITE_SOLUTION_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("carminite_solution_bucket"));

    public static final DeferredItem<BucketItem> FIERY_TEAR_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("fiery_tear_bucket"));

    public static void register(IEventBus modBus) {}
}
