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

package com.adonis.createshimmer.integration.jei.category;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.grindstone.GrindingRecipe;
import com.adonis.createshimmer.common.kinetics.grindstone.MechanicalGrindStoneItem;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.config.CSConfig;
import com.adonis.createshimmer.integration.jei.category.grinding.GrindingCategory;
import com.adonis.createshimmer.integration.jei.category.printing.*;
import com.google.common.base.Preconditions;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import plus.dragons.createdragonsplus.util.ErrorMessages;

@JeiPlugin
public class CSJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = CSCommon.asResource("jei");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new PrintingCategory(),
                new GrindingCategory());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = getRecipeManager();
        registration.addRecipes(PrintingCategory.TYPE, recipeManager
                .getAllRecipesFor(CSRecipes.PRINTING.getType())
                .stream()
                .map(StandardPrintingRecipeJEI::new)
                .collect(Collectors.toList()));
        List<PrintingRecipeJEI> builtinPrinting = new ArrayList<>();
        if (CSConfig.fluids().enablePackageAddressPrinting.get()) builtinPrinting.add(AddressPrintingRecipeJEI.INSTANCE);
        if (CSConfig.fluids().enablePackagePatternPrinting.get()) builtinPrinting.add(PatternPrintingRecipeJEI.INSTANCE);
        if (CSConfig.fluids().enableCreateCopiableItemPrinting.get()) builtinPrinting.add(CopyPrintingRecipeJEI.INSTANCE);
        if (CSConfig.fluids().enableCustomNamePrinting.get()) builtinPrinting.add(CustomNamePrintingRecipeJEI.INSTANCE);
        if (CSConfig.fluids().enableWrittenBookPrinting.get()) builtinPrinting.add(WrittenBookPrintingRecipeJEI.INSTANCE);
        if (!builtinPrinting.isEmpty())
            registration.addRecipes(PrintingCategory.TYPE, builtinPrinting);
        if (CSConfig.fluids().enableEnchantedBookPrinting.get())
            registration.addRecipes(PrintingCategory.TYPE, EnchantedBookPrintingRecipeJEI.listAll());
        var manualApplication = registration
                .getJeiHelpers()
                .getRecipeType(Create.asResource("item_application"), ItemApplicationRecipe.class)
                .orElseThrow();
        registration.addRecipes(manualApplication, List.of(MechanicalGrindStoneItem.createRecipe()));
        registration.addRecipes(GrindingCategory.TYPE, recipeManager
                .getAllRecipesFor(CSRecipes.GRINDING.getType()));
        RecipeType<SandPaperPolishingRecipe> polishing = AllRecipeTypes.SANDPAPER_POLISHING.getType();
        registration.addRecipes(GrindingCategory.TYPE, recipeManager
                .getAllRecipesFor(polishing)
                .stream()
                .map(GrindingRecipe::fromPolishing)
                .flatMap(Optional::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(PrintingCategory.TYPE, CSBlocks.PRINTER);
        registration.addRecipeCatalysts(GrindingCategory.TYPE, CSBlocks.MECHANICAL_GRINDSTONE);
    }

    @Internal
    public static RecipeManager getRecipeManager() {
        if (FMLLoader.getDist() != Dist.CLIENT)
            throw new IllegalStateException("Retreiving recipe manager from client level is only supported for client");
        var minecraft = Minecraft.getInstance();
        Preconditions.checkNotNull(minecraft, ErrorMessages.notNull("minecraft"));
        var level = minecraft.level;
        Preconditions.checkNotNull(level, ErrorMessages.notNull("level"));
        return level.getRecipeManager();
    }
}
