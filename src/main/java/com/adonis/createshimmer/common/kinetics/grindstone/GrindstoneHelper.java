/*
 * Copyright (C) 2025 DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.adonis.createshimmer.common.kinetics.grindstone;

import com.adonis.createshimmer.common.registry.CSRecipes;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GrindstoneEvent;

public class GrindstoneHelper {
    public static boolean canItemBeGrinded(Level level, ItemStack top, ItemStack bottom) {
        var event = NeoForge.EVENT_BUS.post(new GrindstoneEvent.OnPlaceItem(top, bottom, -1));
        if (event.isCanceled())
            return false;
        if (!event.getOutput().isEmpty())
            return true;
        return !computeResult(top, bottom).isEmpty();
    }

    public static Optional<Result> grindItem(Level level, ItemStack top, ItemStack bottom) {
        var place = NeoForge.EVENT_BUS.post(new GrindstoneEvent.OnPlaceItem(top, ItemStack.EMPTY, -1));
        if (place.isCanceled())
            return Optional.empty();
        int experience = place.getXp();
        var output = place.getOutput();
        if (output.isEmpty()) {
            output = computeResult(top, bottom);
            if (output.isEmpty())
                return Optional.empty();
            if (experience == -1) {
                experience = getGrindingExperience(level, top, bottom);
            }
        }
        var take = NeoForge.EVENT_BUS.post(new GrindstoneEvent.OnTakeItem(top, bottom, experience));
        if (take.isCanceled()) {
            return Optional.of(new Result(top, bottom, output, 0));
        }
        return Optional.of(new Result(take.getNewTopItem(), take.getNewBottomItem(), output, Math.max(take.getXp(), 0)));
    }

    private static int getGrindingExperience(Level level, ItemStack top, ItemStack bottom) {
        int experience = 0;
        experience += getExperienceFromItem(top);
        experience += getExperienceFromItem(bottom);
        if (experience > 0) {
            int average = Mth.ceil(experience / 2.0);
            return average + level.random.nextInt(average);
        } else {
            return 0;
        }
    }

    public static int getExperienceFromItem(ItemStack stack) {
        int result = 0;
        ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        for (Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            int level = entry.getIntValue();
            if (!holder.is(EnchantmentTags.CURSE)) {
                result += holder.value().getMinCost(level);
            }
        }
        return result;
    }

    public static int getExperienceFromGrindingRecipe(Level level, ItemStack stack) {
        var input = new SingleRecipeInput(stack);
        var grinding = SequencedAssemblyRecipe.getRecipe(level, input, CSRecipes.GRINDING.getType(), GrindingRecipe.class);
        if (grinding.isEmpty())
            grinding = level.getRecipeManager().getRecipeFor(CSRecipes.GRINDING.getType(), input, level);
        return grinding.map(grindingRecipeRecipeHolder -> grindingRecipeRecipeHolder.value().getFluidResults().getFirst().getAmount()).orElse(0);
    }

    private static ItemStack computeResult(ItemStack top, ItemStack bottom) {
        boolean topEmpty = top.isEmpty();
        boolean bottomEmpty = bottom.isEmpty();
        if (topEmpty && bottomEmpty) {
            return ItemStack.EMPTY;
        } else if (top.getCount() <= 1 && bottom.getCount() <= 1) {
            if (topEmpty || bottomEmpty) {
                ItemStack input = topEmpty ? bottom : top;
                return !EnchantmentHelper.hasAnyEnchantments(input)
                        ? ItemStack.EMPTY
                        : removeNonCursesFrom(input.copy());
            } else {
                return mergeItems(top, bottom);
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack mergeItems(ItemStack top, ItemStack bottom) {
        if (!top.is(bottom.getItem())) {
            return ItemStack.EMPTY;
        } else {
            int maxDamage = Math.max(top.getMaxDamage(), bottom.getMaxDamage());
            int topDurability = top.getMaxDamage() - top.getDamageValue();
            int bottomDurability = bottom.getMaxDamage() - bottom.getDamageValue();
            int l = topDurability + bottomDurability + maxDamage * 5 / 100;
            int count = 1;
            if (!top.isDamageableItem() || !top.isRepairable()) {
                if (top.getMaxStackSize() < 2 || !ItemStack.matches(top, bottom)) {
                    return ItemStack.EMPTY;
                }

                count = 2;
            }

            ItemStack result = top.copyWithCount(count);
            if (result.isDamageableItem()) {
                result.set(DataComponents.MAX_DAMAGE, maxDamage);
                result.setDamageValue(Math.max(maxDamage - l, 0));
                if (!bottom.isRepairable())
                    result.setDamageValue(top.getDamageValue());
            }

            mergeEnchantsFrom(result, bottom);
            return removeNonCursesFrom(result);
        }
    }

    private static void mergeEnchantsFrom(ItemStack top, ItemStack bottom) {
        EnchantmentHelper.updateEnchantments(top, topEnchantments -> {
            ItemEnchantments bottomEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(bottom);

            for (Entry<Holder<Enchantment>> entry : bottomEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (!holder.is(EnchantmentTags.CURSE) || topEnchantments.getLevel(holder) == 0) {
                    topEnchantments.upgrade(holder, entry.getIntValue());
                }
            }
        });
    }

    public static ItemStack removeNonCursesFrom(ItemStack input) {
        ItemEnchantments itemenchantments = EnchantmentHelper.updateEnchantments(input,
                enchantments -> enchantments.removeIf(enchantment -> !enchantment.is(EnchantmentTags.CURSE)));
        if (input.is(Items.ENCHANTED_BOOK) && itemenchantments.isEmpty()) {
            input = input.transmuteCopy(Items.BOOK);
        }

        int repairCost = 0;

        for (int j = 0; j < itemenchantments.size(); j++) {
            repairCost = AnvilMenu.calculateIncreasedRepairCost(repairCost);
        }

        input.set(DataComponents.REPAIR_COST, repairCost);
        return input;
    }

    public record Result(ItemStack top, ItemStack bottom, ItemStack output, int experience) {}
}
