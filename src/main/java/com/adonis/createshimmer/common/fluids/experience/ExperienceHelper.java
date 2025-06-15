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

package com.adonis.createshimmer.common.fluids.experience;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.fluids.FluidStack;
import com.adonis.createshimmer.common.registry.CEIDataMaps;
import com.adonis.createshimmer.common.registry.CEIFluids;

public class ExperienceHelper {
    public static int getExperienceForNextLevel(int level) {
        if (level >= 30)
            return 9 * level - 158;
        if (level >= 15)
            return 5 * level - 38;
        return 2 * level + 7;
    }

    public static int getExperienceForTotalLevel(int level) {
        if (level == 0)
            return 0;
        if (level >= 31)
            return (9 * level * level - 325 * level) / 2 + 2220;
        if (level >= 16)
            return (5 * level * level - 91 * level) / 2 + 360;
        return level * level + 6 * level;
    }

    public static int getExperienceForPlayer(Player player) {
        int experience = getExperienceForTotalLevel(player.experienceLevel);
        experience += Math.round(player.experienceProgress * getExperienceForNextLevel(player.experienceLevel));
        return experience;
    }

    public static int getExperienceFromFluid(FluidStack fluid) {
        if (fluid.isEmpty()) return 0;
        if (fluid.is(CEIFluids.EXPERIENCE)) return fluid.getAmount();
        int amount = fluid.getAmount();
        Integer unit = fluid.getFluidHolder().getData(CEIDataMaps.FLUID_UNIT_EXPERIENCE);
        if (unit == null)
            return 0;
        return amount / unit;
    }

    public static int getFluidFromExperience(FluidStack fluid, int amount) {
        return getFluidFromExperience(fluid.getFluidHolder(), amount);
    }

    public static int getFluidFromExperience(Holder<Fluid> fluid, int amount) {
        return getExperienceFluidUnit(fluid) * amount;
    }

    public static int getExperienceFluidUnit(Holder<Fluid> fluid) {
        if (fluid.equals(CEIFluids.EXPERIENCE))
            return 1;
        Integer unit = fluid.getData(CEIDataMaps.FLUID_UNIT_EXPERIENCE);
        return unit == null ? 0 : unit;
    }

    public static void award(int amount, ServerPlayer player) {
        amount = repairPlayerItems(player, amount);
        player.giveExperiencePoints(amount);
    }

    public static boolean canRepairItem(ItemStack stack) {
        if (!stack.isDamaged())
            return false;
        var lookup = CommonHooks.resolveLookup(Registries.ENCHANTMENT);
        if (lookup == null)
            return false;
        ItemEnchantments enchantments = stack.getAllEnchantments(lookup);
        for (var enchantment : enchantments.keySet()) {
            if (enchantment.value().effects().has(EnchantmentEffectComponents.REPAIR_WITH_XP))
                return true;
        }
        return false;
    }

    public static int repairItem(int amount, ServerLevel level, ItemStack stack, boolean simulate) {
        int repairing = EnchantmentHelper.modifyDurabilityToRepairFromXp(level, stack, (int) (amount * stack.getXpRepairRatio()));
        int repaired = Math.min(repairing, stack.getDamageValue());
        if (repaired == 0)
            return 0;
        if (!simulate) {
            stack.setDamageValue(stack.getDamageValue() - repaired);
        }
        return Math.max(1, repaired * amount / repairing);
    }

    public static int repairPlayerItems(ServerPlayer player, int amount) {
        Optional<EnchantedItemInUse> optional = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, player, ItemStack::isDamaged);
        if (optional.isPresent()) {
            ItemStack stack = optional.get().itemStack();
            int consumed = repairItem(amount, player.serverLevel(), stack, false);
            return amount > consumed
                    ? repairPlayerItems(player, amount - consumed)
                    : 0;
        }
        return amount;
    }
}
