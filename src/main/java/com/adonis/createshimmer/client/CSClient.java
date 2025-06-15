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

package com.adonis.createshimmer.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import com.adonis.createshimmer.client.model.CSPartialModels;
//import plus.dragons.createenchantmentindustry.client.ponder.CEIPonderPlugin;
import com.adonis.createshimmer.common.CSCommon;

@Mod(value = CSCommon.ID, dist = Dist.CLIENT)
public class CSClient {
    public CSClient(IEventBus modBus) {
        // CEIPartialModels must be registered here,
        // or when PartialModelEventHandler#onRegisterAdditional triggered,
        // PartialModel.ALL won't include all partial model in 'some cases'
        // AllPartialModels#ini does not do this since AllPartialModels is already triggered at AllBlocks.TRACK
        CSPartialModels.register();
        modBus.addListener(CSClient::setup);
    }

    public static void setup(final FMLClientSetupEvent event) {
//        PonderIndex.addPlugin(new CEIPonderPlugin());
    }
}
