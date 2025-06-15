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

package com.adonis.createshimmer.mixin;

import com.adonis.createshimmer.common.fluids.experience.ExperienceHelper;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectivityHandler.class)
public class ConnectivityHandlerMixin {
    @Inject(method = "splitMultiAndInvalidate", at = @At(value = "RETURN", ordinal = 2))
    private static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMulti$dropExperienceFluidSingle(T be, @Coerce Object cache, boolean tryReconnect, CallbackInfo ci) {
        if (!(be.getLevel() instanceof ServerLevel level && be.isRemoved()))
            return;
        if (!(be instanceof IMultiBlockEntityContainer.Fluid fluidContainer))
            return;
        if (!fluidContainer.hasTank() || fluidContainer.getTank(0) instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank)
            return;
        var dropped = fluidContainer.getFluid(0);
        int experience = ExperienceHelper.getExperienceFromFluid(dropped);
        if (experience > 0) {
            var state = be.getBlockState();
            var pos = be.getBlockPos();
            state.getBlock().popExperience(level, pos, experience);
        }
    }

    @Inject(method = "splitMultiAndInvalidate", at = @At("TAIL"))
    private static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMulti$dropExperienceFluidMulti(T be, @Coerce Object cache, boolean tryReconnect, CallbackInfo ci, @Local FluidStack dropped) {
        if (!(be.getLevel() instanceof ServerLevel level))
            return;
        if (!(be instanceof IMultiBlockEntityContainer.Fluid fluidContainer))
            return;
        if (!fluidContainer.hasTank() || fluidContainer.getTank(0) instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank)
            return;
        int experience = ExperienceHelper.getExperienceFromFluid(dropped);
        if (experience > 0) {
            var state = be.getBlockState();
            var pos = be.getBlockPos();
            state.getBlock().popExperience(level, pos, experience);
        }
    }
}
