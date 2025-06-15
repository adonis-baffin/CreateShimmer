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

package com.adonis.createshimmer.integration.jei.category.printing;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.adonis.createshimmer.config.CSConfig;
import com.adonis.createshimmer.util.CSLang;
import com.mojang.serialization.MapCodec;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import plus.dragons.createdragonsplus.util.Pairs;

public enum CustomNamePrintingRecipeJEI implements PrintingRecipeJEI {
    INSTANCE;

    public static final PrintingRecipeJEI.Type TYPE = PrintingRecipeJEI
            .register(CSCommon.asResource("custom_name"), MapCodec.unit(INSTANCE));

    @Override
    public void setBase(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.NAME_TAG);
        slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(CSLang
                .translate("recipe.printing.custom_name.base")
                .style(ChatFormatting.GRAY)
                .component()));
    }

    @Override
    public void setTemplate(IRecipeSlotBuilder slot) {
        var stack = new ItemStack(Items.NAME_TAG);
        var name = CSLang.translate("recipe.printing.custom_name.template").component();
        stack.set(DataComponents.CUSTOM_NAME, name);
        slot.addItemStack(stack);
    }

    @Override
    public void setFluid(IRecipeSlotBuilder slot) {
        CSDataMaps.getSourceFluidEntries(CSDataMaps.PRINTING_CUSTOM_NAME_INGREDIENT)
                .forEach(Pairs.accept(slot::addFluidStack));
    }

    @Override
    public void setOutput(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.NAME_TAG);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public void onDisplayedIngredientsUpdate(IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable fluidSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        var name = CSLang.translate("recipe.printing.custom_name.template").component();
        var fluidStack = fluidSlot.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).orElse(FluidStack.EMPTY);
        var style = fluidStack.getFluidHolder().getData(CSDataMaps.PRINTING_CUSTOM_NAME_STYLE);
        if (style != null)
            name.withStyle(style);
        var stack = new ItemStack(Items.NAME_TAG);
        if (CSConfig.fluids().printingCustomNameAsItemName.get())
            stack.set(DataComponents.ITEM_NAME, name);
        else
            stack.set(DataComponents.CUSTOM_NAME, name);
        outputSlot.createDisplayOverrides().addItemStack(stack);
    }
}
