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

package com.adonis.createshimmer.common.fluids.printer.behaviour;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.fluids.experience.ExperienceHelper;
import com.adonis.createshimmer.common.fluids.printer.PrinterBlockEntity;
import com.adonis.createshimmer.common.processing.enchanter.CSEnchantmentHelper;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.adonis.createshimmer.common.registry.CSEnchantments;
import com.adonis.createshimmer.config.CSConfig;
import com.adonis.createshimmer.util.CSIntIntPair;
import com.adonis.createshimmer.util.CSLang;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class EnchantedBookPrintingBehaviour implements PrintingBehaviour {
    private final Level level;
    private final SmartFluidTankBehaviour tank;
    private final ItemStack original;
    private final ItemEnchantments enchantments;
    private final int cost;

    private EnchantedBookPrintingBehaviour(Level level, SmartFluidTankBehaviour tank, ItemStack original, ItemEnchantments enchantments) {
        this.level = level;
        this.tank = tank;
        this.original = original;
        this.enchantments = enchantments;
        AtomicInteger result = new AtomicInteger(0);
        enchantments.entrySet().forEach(entry -> {
            Optional<CSIntIntPair> optional = Optional.empty();
            var customCost = entry.getKey().getData(CSDataMaps.PRINTING_ENCHANTED_BOOK_COST);
            if (customCost != null) {
                optional = customCost.stream().filter(pair -> pair.level() == entry.getIntValue()).findFirst();
            }
            result.addAndGet(optional.map(CSIntIntPair::value).orElseGet(() -> (int) (CSEnchantmentHelper.getEnchantmentCost(entry.getKey(), entry.getIntValue()) * CSConfig.fluids().printingEnchantedBookCostMultiplier.get())));
        });
        this.cost = result.get();
    }

    public static Optional<DataResult<PrintingBehaviour>> create(Level level, SmartFluidTankBehaviour tank, ItemStack stack) {
        if (!stack.is(Items.ENCHANTED_BOOK))
            return Optional.empty();
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        if (enchantments.isEmpty())
            return Optional.of(DataResult.error(() -> CSCommon.asLocalization("gui.printer.enchanted_book.invalid")));
        if (enchantments.keySet().stream().anyMatch(enchantment -> enchantment.is(CSEnchantments.MOD_TAGS.printingDeny))) {
            if (CSConfig.fluids().printingEnchantedBookDenylistStopCopying.get()) {
                return Optional.of(DataResult.error(() -> CSCommon.asLocalization("gui.printer.enchanted_book.denied")));
            } else {
                var rest = enchantments.keySet().stream().filter(enchantment -> !enchantment.is(CSEnchantments.MOD_TAGS.printingDeny)).toList();
                if (rest.isEmpty())
                    return Optional.of(DataResult.error(() -> CSCommon.asLocalization("gui.printer.enchanted_book.all_denied")));
                else {
                    var result = new ItemEnchantments.Mutable(enchantments);
                    result.removeIf(rest::contains);
                    return Optional.of(DataResult.success(new EnchantedBookPrintingBehaviour(level, tank, stack, result.toImmutable())));
                }
            }
        }
        return Optional.of(DataResult.success(new EnchantedBookPrintingBehaviour(level, tank, stack, enchantments)));
    }

    private OptionalInt getCost(FluidStack fluid) {
        int cost = this.cost;
        cost = ExperienceHelper.getFluidFromExperience(fluid, cost);
        if (cost == 0)
            return OptionalInt.empty();
        return OptionalInt.of(cost);
    }

    @Override
    public boolean isValid() {
        if (this.cost <= 0)
            return false;
        var fluid = tank.getPrimaryHandler().getFluid();
        if (fluid.isEmpty())
            return true;
        var cost = getCost(fluid);
        return cost.isPresent() && cost.getAsInt() <= CSConfig.fluids().printerFluidCapacity.get();
    }

    @Override
    public boolean isSafeNBT() {
        return false; // Prevent getting enchantments from nowhere
    }

    @Override
    public int getRequiredItemCount(Level level, ItemStack stack) {
        if (stack.is(Items.BOOK))
            return 1;
        return 0;
    }

    @Override
    public int getRequiredFluidAmount(Level level, ItemStack stack, FluidStack fluidStack) {
        return getCost(fluidStack).orElse(0);
    }

    @Override
    public ItemStack getResult(Level level, ItemStack stack, FluidStack fluidStack) {
        var result = stack.transmuteCopy(Items.ENCHANTED_BOOK, 1);
        result.set(DataComponents.STORED_ENCHANTMENTS, enchantments);
        return result;
    }

    @Override
    public void onFinished(Level level, BlockPos pos, PrinterBlockEntity printer) {
        level.playSound(null, pos.below(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS,
                1.0F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CSLang.translate("gui.goggles.printing").forGoggles(tooltip);
        CSLang.item(original).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
        getCost(tank.getPrimaryHandler().getFluid()).ifPresent(cost -> CSLang.translate("gui.goggles.printing.cost",
                CSLang.number(cost)
                        .add(CreateLang.translate("generic.unit.millibuckets"))
                        .style(cost <= CSConfig.fluids().printerFluidCapacity.get()
                                ? ChatFormatting.GREEN
                                : ChatFormatting.RED))
                .style(ChatFormatting.GRAY).forGoggles(tooltip));
        HolderLookup.Provider registries = level.registryAccess();
        var order = registries
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.TOOLTIP_ORDER)
                .map(holders -> (HolderSet<Enchantment>) holders)
                .orElse(HolderSet.direct());
        for (Holder<Enchantment> ordered : order) {
            int level = enchantments.getLevel(ordered);
            if (level > 0) {
                CSLang.builder().add(Enchantment.getFullname(ordered, level)).forGoggles(tooltip, 1);
            }
        }
        for (Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> unordered = entry.getKey();
            int level = entry.getIntValue();
            if (level > 0 && !order.contains(unordered)) {
                CSLang.builder().add(Enchantment.getFullname(unordered, level)).forGoggles(tooltip, 1);
            }
        }
        return true;
    }
}
