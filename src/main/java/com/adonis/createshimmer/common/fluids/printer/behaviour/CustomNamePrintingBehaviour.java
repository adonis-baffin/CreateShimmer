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

import com.adonis.createshimmer.common.fluids.printer.PrinterBlockEntity;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.adonis.createshimmer.config.CSConfig;
import com.adonis.createshimmer.util.CSLang;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class CustomNamePrintingBehaviour implements PrintingBehaviour {
    private final SmartFluidTankBehaviour tank;
    private final Component name;

    private CustomNamePrintingBehaviour(SmartFluidTankBehaviour tank, Component name) {
        this.tank = tank;
        this.name = name;
    }

    public static Optional<DataResult<PrintingBehaviour>> create(Level level, SmartFluidTankBehaviour tank, ItemStack stack) {
        if (stack.is(Items.NAME_TAG)) {
            var name = stack.get(DataComponents.CUSTOM_NAME);
            if (name == null)
                return Optional.empty();
            return Optional.of(DataResult.success(new CustomNamePrintingBehaviour(tank, name.copy())));
        }
        return Optional.empty();
    }

    @Override
    public int getRequiredItemCount(Level level, ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public int getRequiredFluidAmount(Level level, ItemStack stack, FluidStack fluidStack) {
        var amount = fluidStack.getFluidHolder().getData(CSDataMaps.PRINTING_CUSTOM_NAME_INGREDIENT);
        return amount == null ? 0 : amount;
    }

    @Override
    public ItemStack getResult(Level level, ItemStack stack, FluidStack fluidStack) {
        var result = stack.copy();
        var name = getCustomName(fluidStack);
        if (CSConfig.fluids().printingCustomNameAsItemName.get())
            result.set(DataComponents.ITEM_NAME, name);
        else
            result.set(DataComponents.CUSTOM_NAME, name);
        return result;
    }

    @Override
    public void onFinished(Level level, BlockPos pos, PrinterBlockEntity printer) {
        // Plays SoundEvents.ANVIL_USE
        level.levelEvent(1030, pos.below(), 0);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var fluidStack = this.tank.getPrimaryHandler().getFluid();
        var name = getCustomName(fluidStack);
        if (!CSConfig.fluids().printingCustomNameAsItemName.get())
            name.withStyle(ChatFormatting.ITALIC);
        CSLang.translate("gui.goggles.printing.custom_name").forGoggles(tooltip);
        CSLang.builder().add(name).forGoggles(tooltip, 1);
        var cost = tank.getPrimaryHandler().getFluid().getFluidHolder().getData(CSDataMaps.PRINTING_PATTERN_INGREDIENT);
        if (cost != null)
            CSLang.translate("gui.goggles.printing.cost",
                    CSLang.number(cost)
                            .add(CreateLang.translate("generic.unit.millibuckets"))
                            .style(cost <= CSConfig.fluids().printerFluidCapacity.get()
                                    ? ChatFormatting.GREEN
                                    : ChatFormatting.RED))
                    .forGoggles(tooltip, 1);
        else if (!tank.getPrimaryHandler().getFluid().isEmpty()) {
            CSLang.translate("gui.goggles.printing.incorrect_liquid").style(ChatFormatting.RED).forGoggles(tooltip);
        }
        return true;
    }

    private MutableComponent getCustomName(FluidStack fluidStack) {
        var name = this.name.copy();
        var style = fluidStack.getFluidHolder().getData(CSDataMaps.PRINTING_CUSTOM_NAME_STYLE);
        if (style != null)
            name.withStyle(style);
        return name;
    }
}
