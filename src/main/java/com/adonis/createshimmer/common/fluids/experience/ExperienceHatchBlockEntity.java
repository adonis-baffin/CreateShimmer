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
//package plus.dragons.createenchantmentindustry.common.fluids.experience;
//
//import com.simibubi.create.content.logistics.itemHatch.HatchFilterSlot;
//import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
//import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
//import java.util.List;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class ExperienceHatchBlockEntity extends SmartBlockEntity {
//    public ExperienceHatchBehaviour behaviour;
//
//    public ExperienceHatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
//        behaviours.add(behaviour = new ExperienceHatchBehaviour(this, new HatchFilterSlot()));
//    }
//}
