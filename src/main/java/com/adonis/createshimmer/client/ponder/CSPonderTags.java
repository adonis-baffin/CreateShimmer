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

package com.adonis.createshimmer.client.ponder;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.ARM_TARGETS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.CONTRAPTION_ACTOR;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import plus.dragons.createdragonsplus.common.registry.CDPBlockEntities;

public class CSPonderTags {
    public static final ResourceLocation EXPERIENCE_APPLIANCES = CSCommon.asResource("experience_appliances");
    public static final ResourceLocation SUPER_EXPERIENCE_APPLIANCES = CSCommon.asResource("super_experience_related");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> entryHelper = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(EXPERIENCE_APPLIANCES)
                .addToIndex()
                .item(AllBlocks.EXPERIENCE_BLOCK.get(), true, false)
                .title("Experience Related")
                .description("Components which will be used when processing and applying Experience")
                .register();

        helper.registerTag(SUPER_EXPERIENCE_APPLIANCES)
                .addToIndex()
                .item(CSBlocks.SUPER_EXPERIENCE_BLOCK.get(), true, false)
                .title("Super Experience Related")
                .description("Components which will be used when processing and applying Super Experience")
                .register();

        entryHelper.addToTag(EXPERIENCE_APPLIANCES)
                .add(AllBlocks.ITEM_DRAIN)
                .add(AllBlocks.SPOUT)
                .add(CSBlocks.MECHANICAL_GRINDSTONE)
                .add(CDPBlockEntities.FLUID_HATCH)
                .add(CSBlocks.EXPERIENCE_HATCH)
                .add(CSBlocks.EXPERIENCE_LANTERN)
                .add(CSBlocks.BLAZE_ENCHANTER)
                .add(CSBlocks.BLAZE_FORGER)
                .add(CSBlocks.PRINTER);

        entryHelper.addToTag(SUPER_EXPERIENCE_APPLIANCES)
                .add(CSBlocks.BLAZE_ENCHANTER)
                .add(CSBlocks.BLAZE_FORGER);

        entryHelper.addToTag(ARM_TARGETS)
                .add(CSBlocks.BLAZE_ENCHANTER)
                .add(CSBlocks.BLAZE_FORGER);

        entryHelper.addToTag(CONTRAPTION_ACTOR)
                .add(CSBlocks.EXPERIENCE_LANTERN);
    }
}
