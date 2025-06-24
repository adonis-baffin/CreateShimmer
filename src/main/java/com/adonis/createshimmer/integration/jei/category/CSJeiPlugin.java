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
        // 注册 Transmutation 类别
        if (CSConfig.recipes().enableBulkTransmutation.get()) {
            FanTransmutationCategory transmutationCategory = FanTransmutationCategory.create();
            registration.addRecipeCategories(transmutationCategory);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = getRecipeManager();

        // 注册手动应用配方
        var manualApplication = registration
                .getJeiHelpers()
                .getRecipeType(Create.asResource("item_application"), ItemApplicationRecipe.class);

        // 注册 Transmutation 配方
        if (CSConfig.recipes().enableBulkTransmutation.get()) {
            var transmutationRecipes = recipeManager.getAllRecipesFor(CSRecipes.TRANSMUTATION.getType());
            registration.addRecipes(FanTransmutationCategory.TYPE, transmutationRecipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
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
