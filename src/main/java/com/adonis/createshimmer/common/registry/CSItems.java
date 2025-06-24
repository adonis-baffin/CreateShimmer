package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.item.ShimmerReagentItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;

public class CSItems {
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

    public static final DeferredItem<BucketItem> EXPERIENCE_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("experience_bucket"));

    public static final DeferredItem<BucketItem> SHIMMER_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("shimmer_bucket"));

    public static final DeferredItem<BucketItem> CARMINITE_SOLUTION_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("carminite_solution_bucket"));

    public static final DeferredItem<BucketItem> FIERY_TEAR_BUCKET = DeferredItem
            .createItem(CSCommon.asResource("fiery_tear_bucket"));

    public static void register(IEventBus modBus) {}
}
