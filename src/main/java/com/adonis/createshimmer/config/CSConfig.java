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

import net.minecraft.Util;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CSConfig {
    private static final CSClientConfig CLIENT_CONFIG = new CSClientConfig();
    private static final CSServerConfig SERVER_CONFIG = new CSServerConfig();
    private static ModConfigSpec CLIENT_SPEC;
    private static ModConfigSpec SERVER_SPEC;

    public CSConfig(ModContainer modContainer) {
        CLIENT_SPEC = Util.make(new ModConfigSpec.Builder().configure(builder -> {
            CLIENT_CONFIG.registerAll(builder);
            return Unit.INSTANCE;
        }).getValue(), spec -> modContainer.registerConfig(Type.CLIENT, spec));
        SERVER_SPEC = Util.make(new ModConfigSpec.Builder().configure(builder -> {
            SERVER_CONFIG.registerAll(builder);
            return Unit.INSTANCE;
        }).getValue(), spec -> modContainer.registerConfig(Type.SERVER, spec));
    }

    public static CSClientConfig client() {
        return CLIENT_CONFIG;
    }

    public static CSServerConfig server() {
        return SERVER_CONFIG;
    }

    public static CSKineticsConfig kinetics() {
        return SERVER_CONFIG.kinetics;
    }

    public static CSStressConfig stress() {
        return SERVER_CONFIG.kinetics.stressValues;
    }

    public static CSFluidsConfig fluids() {
        return SERVER_CONFIG.fluids;
    }

    public static CSEnchantmentsConfig enchantments() {
        return SERVER_CONFIG.enchantments;
    }

    public static CSProcessingConfig processing() {
        return SERVER_CONFIG.processing;
    }

    @SubscribeEvent
    public void onLoad(ModConfigEvent.Loading event) {
        var spec = event.getConfig().getSpec();
        if (SERVER_SPEC == spec) {
            SERVER_CONFIG.onLoad();
        } else if (CLIENT_SPEC == spec) {
            CLIENT_CONFIG.onLoad();
        }
    }

    @SubscribeEvent
    public void onReload(ModConfigEvent.Reloading event) {
        var spec = event.getConfig().getSpec();
        if (SERVER_SPEC == spec) {
            SERVER_CONFIG.onReload();
        } else if (CLIENT_SPEC == spec) {
            CLIENT_CONFIG.onReload();
        }
    }
}
