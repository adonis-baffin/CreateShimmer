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

package com.adonis.createshimmer.common.processing.enchanter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import plus.dragons.createdragonsplus.common.processing.blaze.BlazeBlockRenderer;

public class BlazeEnchanterRenderer extends BlazeBlockRenderer<BlazeEnchanterBlockEntity> {
    private final ItemRenderer itemRenderer;

    public BlazeEnchanterRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    protected void renderSafe(BlazeEnchanterBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        EnchanterBehaviorTemplateItemRenderer.renderOnBlockEntity(blockEntity, partialTicks, poseStack, bufferSource, light, overlay);
        var item = blockEntity.heldItem;
        if (!item.isEmpty())
            renderItem(blockEntity, item, blockEntity.processingTime, partialTicks, poseStack, bufferSource, light, overlay);
        if (VisualizationManager.supportsVisualization(blockEntity.getLevel()))
            return;
        super.renderSafe(blockEntity, partialTicks, poseStack, bufferSource, light, overlay);
    }

    protected void renderItem(BlazeEnchanterBlockEntity blockEntity, ItemStack item, int processingTime, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        Level level = blockEntity.getLevel();
        assert level != null;
        var blockPos = blockEntity.getBlockPos();
        float renderTicks = AnimationTickHolder.getTicks(level);
        float animation = processingTime == -1
                ? 0
                : Mth.sin((processingTime + partialTicks) / 20f);
        float height = 1.25f + (1 + animation) * .25f;
        float xRot = (renderTicks * 5 + blockPos.getX()) % 360;
        float zRot = (renderTicks * 5 + blockPos.getZ()) % 360;
        poseStack.pushPose();
        poseStack.translate(.5f, height, .5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zRot));
        poseStack.scale(.5f, .5f, .5f);
        itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, level, blockEntity.hashCode());
        poseStack.popPose();
    }
}
