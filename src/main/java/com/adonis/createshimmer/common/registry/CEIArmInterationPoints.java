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

import static com.adonis.createshimmer.common.CEICommon.REGISTRATE;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;
import com.adonis.createshimmer.common.processing.enchanter.BlazeEnchanterArmInteractionPoint;
import com.adonis.createshimmer.common.processing.forger.BlazeForgerArmInteractionPoint;

public class CEIArmInterationPoints {
    public static final RegistryEntry<ArmInteractionPointType, BlazeEnchanterArmInteractionPoint.Type> BLAZE_ENCHANTER = REGISTRATE
            .armInteractionPoint("blaze_enchanter", BlazeEnchanterArmInteractionPoint.Type::new)
            .register();
    public static final RegistryEntry<ArmInteractionPointType, BlazeForgerArmInteractionPoint.Type> BLAZE_FORGER = REGISTRATE
            .armInteractionPoint("blaze_forger", BlazeForgerArmInteractionPoint.Type::new)
            .register();

    public static void register(IEventBus modBus) {}
}
