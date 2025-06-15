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

package com.adonis.createshimmer.common.processing.enchanter.behaviour;

import com.adonis.createshimmer.common.processing.enchanter.CSEnchantmentHelper;
import com.adonis.createshimmer.common.processing.enchanter.EnchantingTemplateItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class TemplateEnchantingBehaviour extends EnchantingBehaviour {
    private final ItemStack target;

    public TemplateEnchantingBehaviour(ItemStack target) {
        this.target = target;
    }

    @Override
    public boolean canProcess(Level level, ItemStack stack, boolean special) {
        if (enchantments.isEmpty())
            return false;
        if (stack.getItem() instanceof EnchantingTemplateItem template) {
            if (stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty())
                return (!special && !template.isSpecial()) || (special && template.isSpecial());
        }
        return false;
    }

    @Override
    public void update(Level level, ItemStack stack, int enchantingLevel, boolean special, boolean cursed) {
        super.update(level, target, enchantingLevel, special, cursed);
    }

    @Override
    public ItemStack getResult(Level level, ItemStack stack, RandomSource random, boolean special) {
        var enchantments = CSEnchantmentHelper.selectEnchantments(random, enchantingLevel, this.enchantments, special);
        if (enchantments.size() > 1)
            enchantments.remove(random.nextInt(enchantments.size()));
        return stack.getItem().applyEnchantments(stack, enchantments);
    }
}
