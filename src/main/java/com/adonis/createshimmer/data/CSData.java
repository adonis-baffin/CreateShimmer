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

package com.adonis.createshimmer.data;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.client.ponder.CSPonderPlugin;
import com.adonis.createshimmer.common.CSCommon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

@Mod(CSCommon.ID)
public class CSData {
    public CSData(IEventBus modBus) {
        if (!DatagenModLoader.isRunningDataGen())
            return;
        REGISTRATE.registerBuiltinLocalization("interface");
        REGISTRATE.registerForeignLocalization();
        REGISTRATE.registerPonderLocalization(CSPonderPlugin::new);
        modBus.register(this);
    }

    @SubscribeEvent
    public void generate(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        var output = generator.getPackOutput();
        var client = event.includeClient();
        var server = event.includeServer();
        generator.addProvider(server, new CSRecipeProvider(output, lookupProvider));
    }
}
