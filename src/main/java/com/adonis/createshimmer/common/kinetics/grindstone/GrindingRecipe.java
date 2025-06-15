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

import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.integration.jei.category.assembly.AssemblyGrindingCategory;
import com.adonis.createshimmer.util.CSLang;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class GrindingRecipe extends StandardProcessingRecipe<SingleRecipeInput> implements IAssemblyRecipe {
    public GrindingRecipe(ProcessingRecipeParams params) {
        super(CSRecipes.GRINDING, params);
        if (fluidIngredients.size() + fluidResults.size() > 1)
            throw new IllegalArgumentException("Grinding recipe can only have either 1 fluid input or 1 fluid result");
    }

    public static StandardProcessingRecipe.Builder<GrindingRecipe> builder(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(GrindingRecipe::new, id);
    }

    public static Optional<RecipeHolder<GrindingRecipe>> fromPolishing(RecipeHolder<SandPaperPolishingRecipe> recipe) {
        if (AllRecipeTypes.CAN_BE_AUTOMATED.test(recipe)) {
            var id = recipe.id().withSuffix("_using_grindstone");
            var polishing = recipe.value();
            var grinding = builder(id)
                    .require(polishing.getIngredients().getFirst())
                    .output(polishing.getRollableResults().getFirst())
                    .build();
            return Optional.of(new RecipeHolder<>(id, grinding));
        }
        return Optional.empty();
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredients.getFirst().test(input.item());
    }

    @Override
    public Component getDescriptionForAssembly() {
        if (fluidIngredients.isEmpty()) {
            return CSLang.translate("recipe.assembly.grinding").component();
        } else {
            List<FluidStack> matchingFluidStacks = fluidIngredients.getFirst().getMatchingFluidStacks();
            if (matchingFluidStacks.isEmpty()) {
                return Component.literal("Invalid");
            }
            return CSLang.translate("recipe.assembly.grinding.needs_fluid",
                    matchingFluidStacks.getFirst().getHoverName()).component();
        }
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> required) {
        required.add(CSBlocks.MECHANICAL_GRINDSTONE);
        required.add(AllBlocks.ITEM_DRAIN);
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {}

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> AssemblyGrindingCategory::new;
    }
}
