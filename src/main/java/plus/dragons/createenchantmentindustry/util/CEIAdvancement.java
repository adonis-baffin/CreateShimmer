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
//package plus.dragons.createenchantmentindustry.util;
//
//import java.util.function.UnaryOperator;
//import net.minecraft.resources.ResourceLocation;
//import org.jetbrains.annotations.NotNull;
//import plus.dragons.createdragonsplus.common.advancements.CDPAdvancement;
//import plus.dragons.createdragonsplus.common.advancements.criterion.BuiltinTrigger;
//import plus.dragons.createenchantmentindustry.common.CEICommon;
//import plus.dragons.createenchantmentindustry.common.registry.CEIAdvancements;
//
//public class CEIAdvancement extends CDPAdvancement {
//    public CEIAdvancement(String id, UnaryOperator<Builder> b) {
//        super(id, b);
//    }
//
//    @Override
//    protected @NotNull BuiltinTrigger add(@NotNull ResourceLocation id) {
//        return CEIAdvancements.BuiltinTriggersQuickDeploy.add(id);
//    }
//
//    @Override
//    protected void addToAdvancementEntries() {
//        CEIAdvancements.ENTRIES.add(this);
//    }
//
//    @Override
//    protected @NotNull ResourceLocation getBackground() {
//        return CEICommon.asResource("textures/block/super_experience_block.png");
//    }
//
//    @Override
//    protected @NotNull String namespace() {
//        return CEICommon.ID;
//    }
//}
