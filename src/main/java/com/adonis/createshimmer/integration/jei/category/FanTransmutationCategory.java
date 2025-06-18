/*
 * Copyright (C) 2025 DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package com.adonis.createshimmer.integration.jei.category;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.fan.transmutation.TransmutationRecipe;
import com.adonis.createshimmer.common.registry.CSFluids;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import java.util.List;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;

public class FanTransmutationCategory extends ProcessingViaFanCategory<TransmutationRecipe> {
    public static final mezz.jei.api.recipe.RecipeType<RecipeHolder<TransmutationRecipe>> TYPE = mezz.jei.api.recipe.RecipeType.createRecipeHolderType(CSRecipes.TRANSMUTATION.getId());

    private FanTransmutationCategory(Info<TransmutationRecipe> info) {
        super(info);
    }

    public static FanTransmutationCategory create() {
        var id = CSCommon.asResource("fan_transmutation");
        var title = Component.translatable("createshimmer.recipe.fan_transmutation");
        var background = new EmptyBackground(178, 72);
        var icon = new DoubleItemIcon(AllItems.PROPELLER::asStack, () -> CSFluids.SHIMMER.getBucket().stream().map(ItemStack::new).findFirst().orElse(ItemStack.EMPTY));
        var catalyst = AllBlocks.ENCASED_FAN.asStack();
        catalyst.set(DataComponents.CUSTOM_NAME,
                Component.translatable("createshimmer.recipe.fan_transmutation.fan")
                        .withStyle(style -> style.withItalic(false)));
        var info = new Info<>(TYPE, title, background, icon, FanTransmutationCategory::getAllRecipes, List.of(() -> catalyst));
        return new FanTransmutationCategory(info);
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        // 渲染 Shimmer 流体方块作为催化剂方块
        GuiGameElement.of((Fluid) CSFluids.SHIMMER.getSource())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
    }

    private static List<RecipeHolder<TransmutationRecipe>> getAllRecipes() {
        var manager = CSJeiPlugin.getRecipeManager();
        return manager.getAllRecipesFor(CSRecipes.TRANSMUTATION.getType());
    }
}
