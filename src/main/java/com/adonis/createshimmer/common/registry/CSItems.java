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
//    public static final ItemEntry<ExperienceNuggetItem> SUPER_EXPERIENCE_NUGGET = REGISTRATE
//            .item("super_experience_nugget", ExperienceNuggetItem::new)
//            .tag(Tags.Items.NUGGETS)
//            .properties(p -> p.rarity(Rarity.RARE))
//            .lang("Nugget of Super Experience")
//            .register();
//    public static final ItemEntry<EnchantingTemplateItem> ENCHANTING_TEMPLATE = REGISTRATE
//            .item("enchanting_template", EnchantingTemplateItem::normal)
//            .properties(prop -> prop
//                    .rarity(Rarity.UNCOMMON)
//                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY))
//            .register();
//    public static final ItemEntry<EnchantingTemplateItem> SUPER_ENCHANTING_TEMPLATE = REGISTRATE
//            .item("super_enchanting_template", EnchantingTemplateItem::special)
//            .properties(prop -> prop
//                    .rarity(Rarity.RARE)
//                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY))
//            .register();
//    public static final ItemEntry<Item> EXPERIENCE_CAKE_BASE = REGISTRATE
//            .item("experience_cake_base", Item::new)
//            .lang("Cake Base o' Enchanting")
//            .tag(AllItemTags.UPRIGHT_ON_BELT.tag)
//            .register();
//    public static final ItemEntry<Item> EXPERIENCE_CAKE = REGISTRATE
//            .item("experience_cake", Item::new)
//            .lang("Cake o' Enchanting")
//            .properties(prop -> prop
//                    .rarity(Rarity.RARE)
//                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
//            .tag(AllItemTags.UPRIGHT_ON_BELT.tag)
//            .register();
//    public static final ItemEntry<Item> EXPERIENCE_CAKE_SLICE = REGISTRATE
//            .item("experience_cake_slice", Item::new)
//            .lang("Cake Slice o' Enchanting")
//            .properties(prop -> prop
//                    .rarity(Rarity.RARE)
//                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
//            .register();
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
                    .rarity(Rarity.COMMON))
            .register();

    public static final ItemEntry<Item> CHARM_OF_SHIMMER_2 = REGISTRATE
            .item("charm_of_shimmer_2", Item::new)
            .lang("Charm of Shimmer II")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON))
            .register();

    public static final ItemEntry<Item> CHARM_OF_SHIMMER_BASE = REGISTRATE
            .item("charm_of_shimmer_base", Item::new)
            .lang("Charm of Shimmer Base")
            .properties(prop -> prop
                    .rarity(Rarity.COMMON))
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
