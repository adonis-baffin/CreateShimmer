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

import com.adonis.createshimmer.client.model.CSPartialModels;
import com.adonis.createshimmer.common.fluids.experience.ExperienceHatchBlockEntity;
import com.adonis.createshimmer.common.fluids.lantern.ExperienceLanternBlockEntity;
import com.adonis.createshimmer.common.fluids.printer.PrinterBlockEntity;
import com.adonis.createshimmer.common.fluids.printer.PrinterRenderer;
import com.adonis.createshimmer.common.kinetics.grindstone.GrindstoneDrainBlockEntity;
import com.adonis.createshimmer.common.kinetics.grindstone.GrindstoneDrainRenderer;
import com.adonis.createshimmer.common.processing.enchanter.BlazeEnchanterBlockEntity;
import com.adonis.createshimmer.common.processing.enchanter.BlazeEnchanterRenderer;
import com.adonis.createshimmer.common.processing.forger.BlazeForgerBlockEntity;
import com.adonis.createshimmer.common.processing.forger.BlazeForgerRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import plus.dragons.createdragonsplus.common.processing.blaze.BlazeBlockVisual;

public class CSBlockEntities {
    public static final BlockEntityEntry<KineticBlockEntity> MECHANICAL_GRINDSTONE = REGISTRATE
            .blockEntity("mechanical_grindstone", KineticBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.of(CSPartialModels.MECHANICAL_GRINDSTONE), false)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .validBlock(CSBlocks.MECHANICAL_GRINDSTONE)
            .register();
    public static final BlockEntityEntry<GrindstoneDrainBlockEntity> GRINDSTONE_DRAIN = REGISTRATE
            .blockEntity("grindstone_drain", GrindstoneDrainBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.of(CSPartialModels.MECHANICAL_GRINDSTONE), true)
            .renderer(() -> GrindstoneDrainRenderer::new)
            .validBlock(CSBlocks.GRINDSTONE_DRAIN)
            .register();
    public static final BlockEntityEntry<ExperienceHatchBlockEntity> EXPERIENCE_HATCH = REGISTRATE
            .blockEntity("experience_hatch", ExperienceHatchBlockEntity::new)
            .renderer(() -> SmartBlockEntityRenderer::new)
            .validBlock(CSBlocks.EXPERIENCE_HATCH)
            .register();
    public static final BlockEntityEntry<PrinterBlockEntity> PRINTER = REGISTRATE
            .blockEntity("printer", PrinterBlockEntity::new)
            .renderer(() -> PrinterRenderer::new)
            .validBlock(CSBlocks.PRINTER)
            .register();
    public static final BlockEntityEntry<BlazeEnchanterBlockEntity> BLAZE_ENCHANTER = REGISTRATE
            .blockEntity("blaze_enchanter", BlazeEnchanterBlockEntity::new)
            .visual(() -> BlazeBlockVisual::new)
            .renderer(() -> BlazeEnchanterRenderer::new)
            .validBlock(CSBlocks.BLAZE_ENCHANTER)
            .register();
    public static final BlockEntityEntry<BlazeForgerBlockEntity> BLAZE_FORGER = REGISTRATE
            .blockEntity("blaze_forger", BlazeForgerBlockEntity::new)
            .visual(() -> BlazeBlockVisual::new)
            .renderer(() -> BlazeForgerRenderer::new)
            .validBlock(CSBlocks.BLAZE_FORGER)
            .register();
    public static final BlockEntityEntry<ExperienceLanternBlockEntity> EXPERIENCE_LANTERN = REGISTRATE
            .blockEntity("experience_lantern", ExperienceLanternBlockEntity::new)
            .validBlock(CSBlocks.EXPERIENCE_LANTERN)
            .register();

    public static void register(IEventBus modBus) {
        modBus.register(CSBlockEntities.class);
    }

    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                GRINDSTONE_DRAIN.get(), GrindstoneDrainBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                GRINDSTONE_DRAIN.get(), GrindstoneDrainBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                PRINTER.get(), PrinterBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                BLAZE_ENCHANTER.get(), BlazeEnchanterBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                BLAZE_FORGER.get(), BlazeForgerBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                EXPERIENCE_LANTERN.get(), ExperienceLanternBlockEntity::getFluidHandler);
    }
}
