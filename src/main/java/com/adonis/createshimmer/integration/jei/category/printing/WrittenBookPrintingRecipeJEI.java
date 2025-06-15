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

package com.adonis.createshimmer.integration.jei.category.printing;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.mojang.serialization.MapCodec;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import net.minecraft.world.item.Items;
import plus.dragons.createdragonsplus.util.Pairs;

public enum WrittenBookPrintingRecipeJEI implements PrintingRecipeJEI {
    INSTANCE;

    public static final PrintingRecipeJEI.Type TYPE = PrintingRecipeJEI
            .register(CSCommon.asResource("written_book"), MapCodec.unit(INSTANCE));

    @Override
    public void setBase(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.BOOK);
    }

    @Override
    public void setTemplate(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.WRITTEN_BOOK);
    }

    @Override
    public void setFluid(IRecipeSlotBuilder slot) {
        CSDataMaps.getSourceFluidEntries(CSDataMaps.PRINTING_WRITTEN_BOOK_INGREDIENT)
                .forEach(Pairs.accept(slot::addFluidStack));
    }

    @Override
    public void setOutput(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.WRITTEN_BOOK);
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
