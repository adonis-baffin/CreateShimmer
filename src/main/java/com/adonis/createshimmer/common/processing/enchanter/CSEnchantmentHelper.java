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

package com.adonis.createshimmer.common.processing.enchanter;

import com.adonis.createshimmer.common.fluids.experience.ExperienceHelper;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.adonis.createshimmer.config.CSConfig;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class CSEnchantmentHelper {
    @Nullable
    public static Function<Holder<Enchantment>, Integer> alternativeMaxLevel;

    public static int getEnchantmentCost(Holder<Enchantment> holder, int level) {
        var enchantment = holder.value();
        int cost = ExperienceHelper.getExperienceForNextLevel(enchantment.getMinCost(level));
        if (level == 1)
            return cost;
        return cost + getEnchantmentCost(holder, level - 1);
    }

    public static int getEnchantmentCost(ItemEnchantments enchantments) {
        return enchantments.entrySet().stream()
                .mapToInt(entry -> getEnchantmentCost(entry.getKey(), entry.getIntValue()))
                .sum();
    }

    public static int getAdjustedLevel(ItemStack stack, int level) {
        var value = stack.getEnchantmentValue();
        if (value > 0)
            level += 1 + value / 4;
        float f = 0.15F;
        level = Mth.clamp(Math.round(level + level * f), 1, Integer.MAX_VALUE);
        return level;
    }

    public static List<EnchantmentInstance> getAvailableEnchantmentResults(int level, Stream<Holder<Enchantment>> possibleEnchantments, boolean special) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        possibleEnchantments.forEach(holder -> {
            Enchantment enchantment = holder.value();
            int maxLevel = maxLevel(holder);
            for (int i = maxLevel; i >= enchantment.getMinLevel(); i--) {
                if (level >= enchantment.getMinCost(i) && level <= enchantment.getMaxCost(i)) {
                    list.add(new EnchantmentInstance(holder, i));
                    break;
                }
            }
        });
        return list;
    }

    public static List<EnchantmentInstance> selectEnchantments(RandomSource random, int adjustedLevel, List<EnchantmentInstance> available, boolean special) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        WeightedRandom.getRandomItem(random, available).ifPresent(list::add);
        while (random.nextInt(50) <= adjustedLevel) {
            if (!list.isEmpty())
                if (special && CSConfig.enchantments().ignoreEnchantmentCompatibility.get()) {
                    available.removeIf(instance -> instance.enchantment.equals(list.getLast().enchantment));
                } else {
                    EnchantmentHelper.filterCompatibleEnchantments(available, list.getLast());
                }
            if (available.isEmpty())
                break;
            WeightedRandom.getRandomItem(random, available).ifPresent(list::add);
            adjustedLevel /= 2;
        }
        return list;
    }

    public static int maxLevel(Holder<Enchantment> enchantment) {
        if (alternativeMaxLevel == null) return enchantment.value().getMaxLevel();
        return alternativeMaxLevel.apply(enchantment);
    }

    public static int levelExtension(Holder<Enchantment> enchantment) {
        var result = enchantment.getData(CSDataMaps.SUPER_ENCHANTING_LEVEL_EXTENSION);
        return result != null ? result.intValue() : CSConfig.enchantments().enchantmentMaxLevelExtension.get();
    }
}
