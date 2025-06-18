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

package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CSServerConfig extends ConfigBase {
    public final CSKineticsConfig kinetics = nested(0, CSKineticsConfig::new, Comments.kinetics);
    public final CSFluidsConfig fluids = nested(0, CSFluidsConfig::new, Comments.fluids);
    public final CSEnchantmentsConfig enchantments = nested(0, CSEnchantmentsConfig::new, Comments.enchantments);
    public final CSProcessingConfig processing = nested(0, CSProcessingConfig::new, Comments.processing);
    public final CSRecipesConfig recipes = nested(0, CSRecipesConfig::new, Comments.recipes);

    @Override
    public void registerAll(ModConfigSpec.Builder builder) {
        super.registerAll(builder);
    }

    @Override
    public String getName() {
        return "server";
    }

    static class Comments {
        static final String kinetics = "Parameters and abilities of kinetic mechanisms";
        static final String fluids = "Parameters and abilities of fluids and fluid operating components";
        static final String enchantments = "Parameters and abilities of enchantment operating components";
        static final String processing = "Parameters and abilities of processing mechanisms and appliances";
        static final String recipes = "Parameters and abilities of recipe processing and fan-based bulk operations";
    }
}
