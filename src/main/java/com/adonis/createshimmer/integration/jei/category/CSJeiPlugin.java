/*
 * Copyright (C) 2025 DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.dragons.createdragonsplus.util.ErrorMessages;

@JeiPlugin
public class CSJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = CSCommon.asResource("jei");
    private static final Logger LOGGER = LoggerFactory.getLogger(CSJeiPlugin.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        try {
            PrintingCategory printingCategory = new PrintingCategory();
            registration.addRecipeCategories(printingCategory);
            // 注册 Grinding 类别
            GrindingCategory grindingCategory = new GrindingCategory();

            registration.addRecipeCategories(grindingCategory);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        try {
            var recipeManager = getRecipeManager();
            var printingRecipes = recipeManager
                    .getAllRecipesFor(CSRecipes.PRINTING.getType())
                    .stream()
                    .map(StandardPrintingRecipeJEI::new)
                    .collect(Collectors.toList());
            registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) printingRecipes);

            // 注册内置 Printing 配方
            List<PrintingRecipeJEI> builtinPrinting = new ArrayList<>();
            if (CSConfig.fluids().enablePackageAddressPrinting.get()) builtinPrinting.add(AddressPrintingRecipeJEI.INSTANCE);
            if (CSConfig.fluids().enablePackagePatternPrinting.get()) builtinPrinting.add(PatternPrintingRecipeJEI.INSTANCE);
            if (CSConfig.fluids().enableCreateCopiableItemPrinting.get()) builtinPrinting.add(CopyPrintingRecipeJEI.INSTANCE);
            if (CSConfig.fluids().enableCustomNamePrinting.get()) builtinPrinting.add(CustomNamePrintingRecipeJEI.INSTANCE);
            if (CSConfig.fluids().enableWrittenBookPrinting.get()) builtinPrinting.add(WrittenBookPrintingRecipeJEI.INSTANCE);
            if (!builtinPrinting.isEmpty())
                registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) builtinPrinting);

            if (CSConfig.fluids().enableEnchantedBookPrinting.get()) {
                var enchantedBookRecipes = EnchantedBookPrintingRecipeJEI.listAll();
                registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) enchantedBookRecipes);
            }

            // 注册手动应用配方
            var manualApplication = registration
                    .getJeiHelpers()
                    .getRecipeType(Create.asResource("item_application"), ItemApplicationRecipe.class);

            if (manualApplication.isPresent()) {
                registration.addRecipes(manualApplication.get(), List.of(MechanicalGrindStoneItem.createRecipe()));
            } else {
            }

            // ===== 关键部分：注册 Grinding 配方 =====

            // 检查 CSRecipes.GRINDING 类型

            // 获取 grinding 配方
            var grindingRecipes = recipeManager.getAllRecipesFor(CSRecipes.GRINDING.getType());

            if (grindingRecipes.isEmpty()) {
                // 由于没有 getRecipeTypes() 方法，我们通过尝试获取已知类型来检查
                var craftingRecipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);
                var smeltingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);
            } else {
                for (var recipe : grindingRecipes) {
                }
            }

            // 注册 grinding 配方
            registration.addRecipes(GrindingCategory.TYPE, grindingRecipes);

            // 获取砂纸抛光配方并转换
            RecipeType<SandPaperPolishingRecipe> polishing = AllRecipeTypes.SANDPAPER_POLISHING.getType();
            var polishingRecipes = recipeManager.getAllRecipesFor(polishing);
            var convertedPolishingRecipes = polishingRecipes
                    .stream()
                    .map(GrindingRecipe::fromPolishing)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
            if (!convertedPolishingRecipes.isEmpty()) {
                registration.addRecipes(GrindingCategory.TYPE, convertedPolishingRecipes);
            }

            // 总计检查
            int totalGrindingRecipes = grindingRecipes.size() + convertedPolishingRecipes.size();

            if (totalGrindingRecipes == 0) {
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        try {
            registration.addRecipeCatalysts(PrintingCategory.TYPE, CSBlocks.PRINTER);
            registration.addRecipeCatalysts(GrindingCategory.TYPE, CSBlocks.MECHANICAL_GRINDSTONE);

        } catch (Exception e) {
            throw e;
        }
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