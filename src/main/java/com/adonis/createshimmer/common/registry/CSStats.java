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

package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import plus.dragons.createdragonsplus.common.CDPRegistrate;
import plus.dragons.createdragonsplus.common.registrate.builder.CustomStatBuilder;

public class CSStats {
    public static final RegistryEntry<ResourceLocation, ResourceLocation> GRINDSTONE_EXPERIENCE = create("mechanical_grindstone_experience")
            .lang("Experience Produced (by Mechanical Grindstone)")
            .register();

    public static final RegistryEntry<ResourceLocation, ResourceLocation> SUPER_ENCHANT = create("super_enchant")
            .lang("Super Enchant")
            .register();

    public static final RegistryEntry<ResourceLocation, ResourceLocation> PRINT = create("print")
            .lang("Printer Used")
            .register();

    public static final RegistryEntry<ResourceLocation, ResourceLocation> FORGE = create("forge")
            .lang("Blaze Forger Used")
            .register();

    public static final RegistryEntry<ResourceLocation, ResourceLocation> ENCHANT = create("enchant")
            .lang("Blaze Enchanter Used")
            .register();

    private static CustomStatBuilder<CDPRegistrate> create(String id) {
        return REGISTRATE.customStat(id, () -> CSCommon.asResource(id));
    }

    public static void register(IEventBus modBus) {}
}
