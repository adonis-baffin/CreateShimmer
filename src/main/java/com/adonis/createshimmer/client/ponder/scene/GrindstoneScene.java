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

package com.adonis.createshimmer.client.ponder.scene;

import com.adonis.createshimmer.client.ponder.CSPonderScenes;
import com.adonis.createshimmer.common.kinetics.grindstone.GrindstoneDrainBlockEntity;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class GrindstoneScene {
    public static void basic(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("mechanical_grindstone.intro", "Introduction to Mechanical Grindstone");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(4, 1, 2, 0, 1, 2), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(50)
                .text("Right-click an Item Drain with Mechanical Grindstone...")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(20);
        scene.overlay().showControls(util.vector().centerOf(2, 2, 2), Pointing.DOWN, 50).rightClick().withItem(CSBlocks.MECHANICAL_GRINDSTONE.asStack());
        scene.idle(20);
        scene.world().setBlock(util.grid().at(2, 1, 2), CSBlocks.GRINDSTONE_DRAIN.getDefaultState().setValue(HorizontalKineticBlock.HORIZONTAL_FACING, Direction.SOUTH), true);
        scene.world().modifyBlockEntity(util.grid().at(2, 1, 2), GrindstoneDrainBlockEntity.class, SmartBlockEntity::markVirtual);
        scene.idle(30);
        scene.overlay().showText(60)
                .text("This is a Grindstone Drain. Place another Mechanical Grindstone on top to use it")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(30);
        scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
        scene.overlay().showOutline(PonderPalette.GREEN, util.select().fromTo(2, 1, 2, 2, 2, 2), util.select().fromTo(2, 1, 2, 2, 2, 2), 30);
        scene.idle(40);
        scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 2, 1)
                .add(util.select().fromTo(0, 1, 3, 4, 2, 4)), Direction.DOWN);
        scene.world().setKineticSpeed(util.select().everywhere(), -64);
        scene.world().setKineticSpeed(util.select().fromTo(2, 2, 2, 2, 2, 3), 64);
        scene.overlay().showText(60)
                .text("Experience in item form can be crushed into liquid form by Mechanical Grindstone")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(2, 2, 2));
        for (int i = 0; i < 4; i++) {
            scene.world().createItemOnBelt(util.grid().at(0, 1, 2), Direction.UP, new ItemStack(i < 2 ? AllItems.EXP_NUGGET : AllBlocks.EXPERIENCE_BLOCK));
            scene.idle(20);
        }
    }

    public static void extra(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("mechanical_grindstone.extra", "Sanding with Mechanical Grindstone");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);
        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(60)
                .text("Mechanical Grindstone not only has all the features of a grindstone...")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.world().setKineticSpeed(util.select().everywhere(), 128);
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 0, 3, 1, 0).add(util.select().position(2, 2, 2)), -128);
        scene.idle(10);
        var sword = new ItemStack(Items.DIAMOND_SWORD);
        CSPonderScenes.enchant(scene, sword, Enchantments.SWEEPING_EDGE, 3);
        scene.world().createItemOnBelt(util.grid().at(0, 1, 0), Direction.UP, sword);
        scene.idle(60);

        scene.overlay().showText(60)
                .text("...but it also has the features of Sand Paper")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(10);
        scene.world().createItemOnBelt(util.grid().at(0, 1, 0), Direction.UP, new ItemStack(AllItems.ROSE_QUARTZ.get()));
        scene.idle(60);

        scene.overlay().showText(60)
                .text("Items can be manually applied to Mechanical Grindstone")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(60);
    }
}
