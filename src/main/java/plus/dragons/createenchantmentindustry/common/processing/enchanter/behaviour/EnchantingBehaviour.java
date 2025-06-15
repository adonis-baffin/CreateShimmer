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

package plus.dragons.createenchantmentindustry.common.processing.enchanter.behaviour;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import plus.dragons.createenchantmentindustry.common.fluids.experience.ExperienceHelper;
import plus.dragons.createenchantmentindustry.common.processing.enchanter.CEIEnchantmentHelper;
import plus.dragons.createenchantmentindustry.common.processing.enchanter.EnchantingTemplateItem;
import plus.dragons.createenchantmentindustry.common.registry.CEIEnchantments;

public class EnchantingBehaviour {
    protected int enchantingLevel;
    protected TagKey<Enchantment> enchantmentTag = CEIEnchantments.MOD_TAGS.enchanting;
    protected List<EnchantmentInstance> enchantments = new ArrayList<>(0);

    protected List<EnchantmentInstance> getAvailableEnchantments(Level level, ItemStack stack, boolean special) {
        int adjustedLevel = CEIEnchantmentHelper.getAdjustedLevel(stack, enchantingLevel);
        if (adjustedLevel == 0)
            return new ArrayList<>(0);
        var possible = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getTag(enchantmentTag)
                .stream()
                .flatMap(HolderSet::stream)
                .filter(stack::isPrimaryItemFor);
        return CEIEnchantmentHelper.getAvailableEnchantmentResults(adjustedLevel, possible, special);
    }

    protected List<EnchantmentInstance> getAvailableCurses(Level level, ItemStack stack) {
        int adjustedLevel = CEIEnchantmentHelper.getAdjustedLevel(stack, enchantingLevel);
        if (adjustedLevel == 0)
            return new ArrayList<>(0);
        var possible = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getTag(EnchantmentTags.CURSE)
                .stream()
                .flatMap(HolderSet::stream)
                .filter(stack::isPrimaryItemFor);
        return CEIEnchantmentHelper.getAvailableEnchantmentResults(adjustedLevel, possible, true);
    }

    public boolean canProcess(Level level, ItemStack stack, boolean special) {
        if (stack.getItem() instanceof EnchantingTemplateItem)
            return false;
        return stack.isEnchantable() && !getAvailableEnchantments(level, stack, special).isEmpty();
    }

    public void update(Level level, ItemStack stack, int enchantingLevel, boolean special, boolean cursed) {
        this.enchantingLevel = enchantingLevel;
        enchantmentTag = special
                ? CEIEnchantments.MOD_TAGS.superEnchanting
                : CEIEnchantments.MOD_TAGS.enchanting;
        enchantments = getAvailableEnchantments(level, stack, special);
        if (!enchantments.isEmpty() && cursed) {
            enchantments.addAll(getAvailableCurses(level, stack));
        }
    }

    public ItemStack getResult(Level level, ItemStack stack, RandomSource random, boolean special) {
        int adjustedLevel = CEIEnchantmentHelper.getAdjustedLevel(stack, enchantingLevel);
        var enchantments = CEIEnchantmentHelper.selectEnchantments(random, adjustedLevel, this.enchantments, special);
        if (stack.is(Items.BOOK) && enchantments.size() > 1) {
            enchantments.remove(random.nextInt(enchantments.size()));
        }
        return stack.getItem().applyEnchantments(stack, enchantments);
    }

    public int getExperienceCost() {
        if (enchantments.isEmpty())
            return 0;
        int levelCost = Math.ceilDiv(enchantingLevel, 10);
        int experienceCost = 0;
        for (int i = 0; i < levelCost; i++) {
            experienceCost += ExperienceHelper.getExperienceForNextLevel(enchantingLevel - i);
        }
        return experienceCost;
    }
}
