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

package com.adonis.createshimmer.common.processing.forger;

import com.adonis.createshimmer.client.model.CSPartialModels;
import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD, modid = CSCommon.ID)
public class BlazeForgerItemRenderer extends CustomRenderedItemModelRenderer {
    @SubscribeEvent
    public static void register(RegisterClientExtensionsEvent event) {
        event.registerItem(
                SimpleCustomRenderer.create(CSBlocks.BLAZE_FORGER.asItem(), new BlazeForgerItemRenderer()),
                CSBlocks.BLAZE_FORGER.asItem());
    }

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);
        poseStack.pushPose();
        poseStack.translate(.5f, .75f, .5f);
        renderer.render(CSPartialModels.BLAZE_FORGER_HAT.get(), light);
        poseStack.popPose();
    }
}
