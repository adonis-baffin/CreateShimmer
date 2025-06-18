/*
 * Copyright (C) 2025 DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package com.adonis.createshimmer.integration.jei.category;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.config.CSConfig;
import com.google.common.base.Preconditions;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
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
//        PrintingCategory printingCategory = new PrintingCategory();
//        registration.addRecipeCategories(printingCategory);
//
//        // 注册 Grinding 类别
//        GrindingCategory grindingCategory = new GrindingCategory();
//        registration.addRecipeCategories(grindingCategory);

        // 注册 Transmutation 类别
        if (CSConfig.recipes().enableBulkTransmutation.get()) {
            FanTransmutationCategory transmutationCategory = FanTransmutationCategory.create();
            registration.addRecipeCategories(transmutationCategory);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = getRecipeManager();

//        // 注册 Printing 配方
//        var printingRecipes = recipeManager
//                .getAllRecipesFor(CSRecipes.PRINTING.getType())
//                .stream()
//                .map(StandardPrintingRecipeJEI::new)
//                .collect(Collectors.toList());
//        registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) printingRecipes);
//
//        // 注册内置 Printing 配方
//        List<PrintingRecipeJEI> builtinPrinting = new ArrayList<>();
//        if (CSConfig.fluids().enablePackageAddressPrinting.get()) builtinPrinting.add(AddressPrintingRecipeJEI.INSTANCE);
//        if (CSConfig.fluids().enablePackagePatternPrinting.get()) builtinPrinting.add(PatternPrintingRecipeJEI.INSTANCE);
//        if (CSConfig.fluids().enableCreateCopiableItemPrinting.get()) builtinPrinting.add(CopyPrintingRecipeJEI.INSTANCE);
//        if (CSConfig.fluids().enableCustomNamePrinting.get()) builtinPrinting.add(CustomNamePrintingRecipeJEI.INSTANCE);
//        if (CSConfig.fluids().enableWrittenBookPrinting.get()) builtinPrinting.add(WrittenBookPrintingRecipeJEI.INSTANCE);
//        if (!builtinPrinting.isEmpty())
//            registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) builtinPrinting);
//
//        if (CSConfig.fluids().enableEnchantedBookPrinting.get()) {
//            var enchantedBookRecipes = EnchantedBookPrintingRecipeJEI.listAll();
//            registration.addRecipes(PrintingCategory.TYPE, (List<PrintingRecipeJEI>)(List<?>) enchantedBookRecipes);
//        }

        // 注册手动应用配方
        var manualApplication = registration
                .getJeiHelpers()
                .getRecipeType(Create.asResource("item_application"), ItemApplicationRecipe.class);

//        if (manualApplication.isPresent()) {
//            registration.addRecipes(manualApplication.get(), List.of(MechanicalGrindStoneItem.createRecipe()));
//        }

//        // 注册 Grinding 配方
//        var grindingRecipes = recipeManager.getAllRecipesFor(CSRecipes.GRINDING.getType());
//        registration.addRecipes(GrindingCategory.TYPE, grindingRecipes);

//        // 获取砂纸抛光配方并转换
//        RecipeType<SandPaperPolishingRecipe> polishing = AllRecipeTypes.SANDPAPER_POLISHING.getType();
//        var polishingRecipes = recipeManager.getAllRecipesFor(polishing);
//        var convertedPolishingRecipes = polishingRecipes
//                .stream()
//                .map(GrindingRecipe::fromPolishing)
//                .flatMap(Optional::stream)
//                .collect(Collectors.toList());
//        if (!convertedPolishingRecipes.isEmpty()) {
//            registration.addRecipes(GrindingCategory.TYPE, convertedPolishingRecipes);
//        }

        // 注册 Transmutation 配方
        if (CSConfig.recipes().enableBulkTransmutation.get()) {
            var transmutationRecipes = recipeManager.getAllRecipesFor(CSRecipes.TRANSMUTATION.getType());
            registration.addRecipes(FanTransmutationCategory.TYPE, transmutationRecipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalysts(PrintingCategory.TYPE, CSBlocks.PRINTER);
//        registration.addRecipeCatalysts(GrindingCategory.TYPE, CSBlocks.MECHANICAL_GRINDSTONE);

        // 注册 Transmutation 催化剂
        if (CSConfig.recipes().enableBulkTransmutation.get()) {
            registration.addRecipeCatalysts(FanTransmutationCategory.TYPE, AllBlocks.ENCASED_FAN);
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
