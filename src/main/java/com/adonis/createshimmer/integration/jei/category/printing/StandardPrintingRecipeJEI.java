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

import com.mojang.serialization.MapCodec;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import com.adonis.createshimmer.common.CEICommon;
import com.adonis.createshimmer.common.fluids.printer.PrintingRecipe;

public class StandardPrintingRecipeJEI implements PrintingRecipeJEI {
    public static final PrintingRecipeJEI.Type TYPE = PrintingRecipeJEI
            .register(CEICommon.asResource("standard"), StandardPrintingRecipeJEI::createCodec);
    private final RecipeHolder<PrintingRecipe> recipe;

    public StandardPrintingRecipeJEI(RecipeHolder<PrintingRecipe> recipe) {
        this.recipe = recipe;
    }

    public static MapCodec<StandardPrintingRecipeJEI> createCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.<RecipeHolder<PrintingRecipe>>getRecipeHolderCodec()
                .xmap(StandardPrintingRecipeJEI::new, jei -> jei.recipe)
                .fieldOf("recipe");
    }

    @Override
    public void setBase(IRecipeSlotBuilder slot) {
        slot.addIngredients(recipe.value().getIngredients().get(0));
    }

    @Override
    public void setTemplate(IRecipeSlotBuilder slot) {
        slot.addIngredients(recipe.value().getIngredients().get(1));
    }

    @Override
    public void setFluid(IRecipeSlotBuilder slot) {
        var fluid = recipe.value().getFluidIngredients().getFirst();
        slot.addIngredients(NeoForgeTypes.FLUID_STACK, fluid.getMatchingFluidStacks());
    }

    @Override
    public void setOutput(IRecipeSlotBuilder slot) {
        slot.addItemStack(recipe.value().getRollableResults().getFirst().getStack());
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return recipe.id();
    }
}
