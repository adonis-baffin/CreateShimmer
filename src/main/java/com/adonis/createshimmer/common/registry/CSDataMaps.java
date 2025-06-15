/*
 * Copyright (C) 2025  DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;
import static net.minecraft.world.item.enchantment.Enchantments.INFINITY;
import static net.minecraft.world.item.enchantment.Enchantments.MENDING;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.fluids.experience.ExperienceFuel;
import com.adonis.createshimmer.util.CSIntIntPair;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import plus.dragons.createdragonsplus.common.registry.CDPFluids;
import plus.dragons.createdragonsplus.util.Pairs;

public class CSDataMaps {
    public static final DataMapType<Item, ExperienceFuel> EXPERIENCE_FUEL = DataMapType
            .builder(CSCommon.asResource("experience_fuel"), Registries.ITEM, ExperienceFuel.CODEC)
            .synced(ExperienceFuel.FULL_CODEC, true)
            .build();
    public static final DataMapType<Fluid, Integer> FLUID_UNIT_EXPERIENCE = DataMapType
            .builder(CSCommon.asResource("unit/experience"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Fluid, Integer> PRINTING_ADDRESS_INGREDIENT = DataMapType
            .builder(CSCommon.asResource("printing/address/ingredient"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Fluid, Integer> PRINTING_PATTERN_INGREDIENT = DataMapType
            .builder(CSCommon.asResource("printing/pattern/ingredient"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Fluid, Integer> PRINTING_COPY_INGREDIENT = DataMapType
            .builder(CSCommon.asResource("printing/copy/ingredient"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Fluid, Integer> PRINTING_CUSTOM_NAME_INGREDIENT = DataMapType
            .builder(CSCommon.asResource("printing/custom_name/ingredient"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Fluid, Style> PRINTING_CUSTOM_NAME_STYLE = DataMapType
            .builder(CSCommon.asResource("printing/custom_name/style"), Registries.FLUID, Style.Serializer.CODEC)
            .synced(Style.Serializer.CODEC, true)
            .build();
    public static final DataMapType<Fluid, Integer> PRINTING_WRITTEN_BOOK_INGREDIENT = DataMapType
            .builder(CSCommon.asResource("printing/written_book/ingredient"), Registries.FLUID, ExtraCodecs.POSITIVE_INT)
            .synced(Codec.INT, true)
            .build();
    public static final DataMapType<Enchantment, List<CSIntIntPair>> PRINTING_ENCHANTED_BOOK_COST = DataMapType
            .builder(CSCommon.asResource("printing/enchanted_book/custom_cost"), Registries.ENCHANTMENT, Codec.list(CSIntIntPair.CODEC))
            .synced(Codec.list(CSIntIntPair.CODEC), true)
            .build();
    public static final DataMapType<Enchantment, Float> FORGING_COST_MULTIPLIER = DataMapType
            .builder(CSCommon.asResource("forging/cost_multiplier"), Registries.ENCHANTMENT, ExtraCodecs.POSITIVE_FLOAT)
            .synced(ExtraCodecs.POSITIVE_FLOAT, true)
            .build();
    public static final DataMapType<Enchantment, Float> SPLITTING_COST_MULTIPLIER = DataMapType
            .builder(CSCommon.asResource("forging/split_enchantment_cost_multiplier"), Registries.ENCHANTMENT, ExtraCodecs.POSITIVE_FLOAT)
            .synced(ExtraCodecs.POSITIVE_FLOAT, true)
            .build();
    public static final DataMapType<Enchantment, Integer> SUPER_ENCHANTING_LEVEL_EXTENSION = DataMapType
            .builder(CSCommon.asResource("super_enchanting/custom_level_extension"), Registries.ENCHANTMENT, ExtraCodecs.NON_NEGATIVE_INT)
            .synced(Codec.INT, true)
            .build();

    public static void register(IEventBus modBus) {
        modBus.register(CSDataMaps.class);
        REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, CSDataMaps::generate);
    }

    @SubscribeEvent
    public static void register(final RegisterDataMapTypesEvent event) {
        event.register(EXPERIENCE_FUEL);
        event.register(FLUID_UNIT_EXPERIENCE);
        event.register(PRINTING_ADDRESS_INGREDIENT);
        event.register(PRINTING_PATTERN_INGREDIENT);
        event.register(PRINTING_COPY_INGREDIENT);
        event.register(PRINTING_CUSTOM_NAME_INGREDIENT);
        event.register(PRINTING_CUSTOM_NAME_STYLE);
        event.register(PRINTING_WRITTEN_BOOK_INGREDIENT);
        event.register(PRINTING_ENCHANTED_BOOK_COST);
        event.register(FORGING_COST_MULTIPLIER);
        event.register(SPLITTING_COST_MULTIPLIER);
        event.register(SUPER_ENCHANTING_LEVEL_EXTENSION);
    }

    public static <T> Stream<Pair<Fluid, T>> getSourceFluidEntries(DataMapType<Fluid, T> type) {
        return BuiltInRegistries.FLUID.getDataMap(type)
                .entrySet()
                .stream()
                .map(Pairs.mapKey(BuiltInRegistries.FLUID::get))
                .filter(Pairs.filterFirst(fluid -> FluidHelper.convertToStill(fluid) == fluid));
    }

    public static void generate(RegistrateDataMapProvider provider) {
        provider.builder(EXPERIENCE_FUEL)
                .add(CSItems.EXPERIENCE_BUCKET, ExperienceFuel.normal(1000, Items.BUCKET.getDefaultInstance()), false) // TODO Temporary solution for Create's bug, See https://github.com/Creators-of-Create/Create/pull/8304
                .add(CSItems.EXPERIENCE_CAKE, ExperienceFuel.special(1000), false)
                .add(CSItems.EXPERIENCE_CAKE_SLICE, ExperienceFuel.special(250), false)
                .add(CSBlocks.SUPER_EXPERIENCE_BLOCK.getId(), ExperienceFuel.special(27), false)
                .add(CSItems.SUPER_EXPERIENCE_NUGGET, ExperienceFuel.special(3), false)
                .add(AllBlocks.EXPERIENCE_BLOCK.getId(), ExperienceFuel.normal(27), false)
                .add(AllItems.EXP_NUGGET, ExperienceFuel.normal(3), false)
                .add(ResourceLocation.fromNamespaceAndPath("create_sa", "heap_of_experience"),
                        ExperienceFuel.normal(12), false,
                        new ModLoadedCondition("create_sa"))
                .add(ResourceLocation.fromNamespaceAndPath("ars_nouveau", "experience_gem"),
                        ExperienceFuel.normal(3), false,
                        new ModLoadedCondition("ars_nouveau"))
                .add(ResourceLocation.fromNamespaceAndPath("ars_nouveau", "greater_experience_gem"),
                        ExperienceFuel.normal(12), false,
                        new ModLoadedCondition("ars_nouveau"))
                .add(ResourceLocation.fromNamespaceAndPath("mysticalagriculture", "experience_droplet"),
                        ExperienceFuel.normal(10), false,
                        new ModLoadedCondition("mysticalagriculture"));
        provider.builder(FLUID_UNIT_EXPERIENCE)
                .add(ResourceLocation.fromNamespaceAndPath("cofh_core", "experience"),
                        25, false,
                        new ModLoadedCondition("cofh_core"))
                .add(ResourceLocation.fromNamespaceAndPath("cyclic", "xpjuice"),
                        20, false,
                        new ModLoadedCondition("cyclic"))
                .add(ResourceLocation.fromNamespaceAndPath("enderio", "xpjuice"),
                        20, false,
                        new ModLoadedCondition("enderio"))
                .add(ResourceLocation.fromNamespaceAndPath("industrialforegoing", "essence"),
                        20, false,
                        new ModLoadedCondition("industrialforegoing"))
                .add(ResourceLocation.fromNamespaceAndPath("mob_grinding_utils", "fluid_xp"),
                        20, false,
                        new ModLoadedCondition("mob_grinding_utils"))
                .add(ResourceLocation.fromNamespaceAndPath("industrialforegoing", "essence"),
                        20, false,
                        new ModLoadedCondition("industrialforegoing"))
                .add(ResourceLocation.fromNamespaceAndPath("pneumaticcraft", "memory_essence"),
                        20, false,
                        new ModLoadedCondition("pneumaticcraft"))
                .add(ResourceLocation.fromNamespaceAndPath("reliquary", "xp_juice_still"),
                        20, false,
                        new ModLoadedCondition("reliquary"))
                .add(ResourceLocation.fromNamespaceAndPath("sophisticatedcore", "xp_still"),
                        20, false,
                        new ModLoadedCondition("sophisticatedcore"));
        var blackDye = CDPFluids.COMMON_TAGS.dyesByColor.get(DyeColor.BLACK);
        provider.builder(PRINTING_ADDRESS_INGREDIENT)
                .add(blackDye, 10, false);
        provider.builder(PRINTING_PATTERN_INGREDIENT)
                .add(blackDye, 100, false);
        provider.builder(PRINTING_COPY_INGREDIENT)
                .add(blackDye, 10, false);
        provider.builder(PRINTING_CUSTOM_NAME_INGREDIENT)
                .add(CSFluids.EXPERIENCE, 10, false)
                .add(CDPFluids.COMMON_TAGS.dyes, 250, false);
        provider.builder(PRINTING_WRITTEN_BOOK_INGREDIENT)
                .add(blackDye, 10, false);
        var customNameStyles = provider.builder(PRINTING_CUSTOM_NAME_STYLE);
        CDPFluids.COMMON_TAGS.dyesByColor.forEach((color, tag) -> customNameStyles
                .add(tag, Style.EMPTY.withColor(color.getTextColor()), false));
        provider.builder(PRINTING_ENCHANTED_BOOK_COST);
        provider.builder(FORGING_COST_MULTIPLIER);
        provider.builder(SPLITTING_COST_MULTIPLIER);
        provider.builder(SUPER_ENCHANTING_LEVEL_EXTENSION)
                .add(MENDING, 0, false)
                .add(INFINITY, 0, false);
        ;
    }
}
