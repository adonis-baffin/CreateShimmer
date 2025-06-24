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

public class CSRepairConfig extends ConfigBase {
    public final ConfigBool enableScepterRepair = b(true, "enableScepterRepair", Comments.enableScepterRepair);
    public final ConfigInt scepterRepairCost = i(50, 1, 1000, "scepterRepairCost", Comments.scepterRepairCost);
    public final ConfigInt scepterRepairAmount = i(9, 1, 100, "scepterRepairAmount", Comments.scepterRepairAmount);
    public final ConfigBool debugMode = b(false, "debugMode", Comments.debugMode);

    @Override
    public String getName() {
        return "scepter_repair";
    }

    static class Comments {
        static final String enableScepterRepair = "Enable shimmer fluid repairing for Twilight Forest scepters";
        static final String scepterRepairCost = "Shimmer fluid cost per repair operation (in mB)";
        static final String scepterRepairAmount = "Durability points restored per repair operation";
        static final String debugMode = "Enable debug logging for scepter repair operations";
    }
}
