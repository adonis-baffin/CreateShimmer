///*
// * Copyright (C) 2025  DragonsPlus
// * SPDX-License-Identifier: LGPL-3.0-or-later
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <https://www.gnu.org/licenses/>.
// */
//
//package plus.dragons.createenchantmentindustry.integration.jei.category.grinding;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import plus.dragons.createenchantmentindustry.common.registry.CEIBlocks;
//
//public class AnimatedGrindstone extends AnimatedKinetics {
//    private final BlockState grindstone = CEIBlocks.MECHANICAL_GRINDSTONE.getDefaultState()
//            .setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
//    private final BlockState drain = CEIBlocks.GRINDSTONE_DRAIN.getDefaultState()
//            .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
//
//    @Override
//    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
//        PoseStack poseStack = graphics.pose();
//        poseStack.pushPose();
//        poseStack.translate(xOffset, yOffset, 0);
//        poseStack.translate(0, 0, 200);
//        poseStack.translate(2, 22, 0);
//        poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
//        poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
//        int scale = 25;
//        blockElement(grindstone)
//                .rotateBlock(0, 0, getCurrentAngle())
//                .atLocal(0, -1, 0)
//                .scale(scale)
//                .render(graphics);
//        blockElement(grindstone)
//                .rotateBlock(0, 0, -getCurrentAngle())
//                .scale(scale)
//                .render(graphics);
//        blockElement(drain)
//                .scale(scale)
//                .render(graphics);
//        poseStack.popPose();
//    }
//}
