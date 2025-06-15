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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface PrintingRecipeJEI {
    @Internal
    BiMap<ResourceLocation, Type> TYPE_BY_ID = HashBiMap.create();
    @Internal
    BiMap<Type, ResourceLocation> ID_BY_TYPE = TYPE_BY_ID.inverse();
    Codec<Type> TYPE_CODEC = ResourceLocation.CODEC.xmap(TYPE_BY_ID::get, ID_BY_TYPE::get);

    static Type register(ResourceLocation id, Type type) {
        TYPE_BY_ID.put(id.withPrefix("printing/"), type);
        return type;
    }

    static Type register(ResourceLocation id, MapCodec<? extends PrintingRecipeJEI> codec) {
        Type type = (codecHelper, recipeManager) -> codec;
        return register(id.withPrefix("printing/"), type);
    }

    void setBase(IRecipeSlotBuilder slot);

    void setTemplate(IRecipeSlotBuilder slot);

    void setFluid(IRecipeSlotBuilder slot);

    void setOutput(IRecipeSlotBuilder slot);

    Type getType();

    default ResourceLocation getRegistryName() {
        var id = ID_BY_TYPE.get(getType());
        if (id == null)
            throw new IllegalStateException(this.getClass() + " does not have its type registered");
        return id;
    }

    default void onDisplayedIngredientsUpdate(IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable fluidSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {}

    @FunctionalInterface
    interface Type {
        MapCodec<? extends PrintingRecipeJEI> codec(ICodecHelper codecHelper, IRecipeManager recipeManager);
    }
}
