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

package com.adonis.createshimmer.common.fluids.lantern;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import com.simibubi.create.api.contraption.storage.fluid.WrapperMountedFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.adonis.createshimmer.common.registry.CEIFluids;
import com.adonis.createshimmer.common.registry.CEIMountedStorageTypes;

public class ExperienceLanternMountedStorage extends WrapperMountedFluidStorage<ExperienceLanternMountedStorage.Handler> {
    public static final MapCodec<ExperienceLanternMountedStorage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("capacity").forGetter(ExperienceLanternMountedStorage::getCapacity),
            FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(ExperienceLanternMountedStorage::getFluid)).apply(i, ExperienceLanternMountedStorage::new));

    private boolean dirty;

    protected ExperienceLanternMountedStorage(MountedFluidStorageType<?> type, int capacity, FluidStack stack) {
        super(type, new ExperienceLanternMountedStorage.Handler(capacity, stack));
        this.wrapped.onChange = () -> this.dirty = true;
    }

    protected ExperienceLanternMountedStorage(int capacity, FluidStack stack) {
        this(CEIMountedStorageTypes.EXPERIENCE_LANTERN.get(), capacity, stack);
    }

    @Override
    public void unmount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof ExperienceLanternBlockEntity lantern) {
            FluidTank inventory = lantern.getTank().getPrimaryHandler();
            inventory.setFluid(this.wrapped.getFluid());
        }
    }

    public FluidStack getFluid() {
        return this.wrapped.getFluid();
    }

    public int getCapacity() {
        return this.wrapped.getCapacity();
    }

    public static ExperienceLanternMountedStorage fromLantern(ExperienceLanternBlockEntity lantern) {
        // tank has update callbacks, make an isolated copy
        FluidTank inventory = lantern.getTank().getPrimaryHandler();
        return new ExperienceLanternMountedStorage(inventory.getCapacity(), inventory.getFluid().copy());
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return super.isFluidValid(tank, stack);
    }

    public static final class Handler extends FluidTank {
        private Runnable onChange = () -> {};

        public Handler(int capacity, FluidStack stack) {
            super(capacity);
            this.setFluid(stack);
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.is(CEIFluids.EXPERIENCE);
        }

        @Override
        protected void onContentsChanged() {
            this.onChange.run();
        }
    }
}
