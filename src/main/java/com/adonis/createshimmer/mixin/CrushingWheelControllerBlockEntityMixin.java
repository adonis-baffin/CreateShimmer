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

package com.adonis.createshimmer.mixin;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.adonis.createshimmer.config.CEIConfig;

@Mixin(CrushingWheelControllerBlockEntity.class)
public class CrushingWheelControllerBlockEntityMixin {
    @Shadow(remap = false)
    public Entity processingEntity;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V", shift = At.Shift.AFTER))
    private void injected(CallbackInfo ci) {
        if (CEIConfig.kinetics().crushingWheelKillDropXp.get() && !processingEntity.isAlive() && processingEntity instanceof LivingEntity livingEntity) {
            if (!(processingEntity.level() instanceof ServerLevel serverLevel)) return;
            int reward = Math.max((int) Math.floor(livingEntity.getExperienceReward(serverLevel, processingEntity) * CEIConfig.kinetics().crushingWheelKillDropXpScale.get()), 1);
            if (reward >= 1000 || Math.random() < CEIConfig.kinetics().crushingWheelKillDropXpChance.get()) {
                int count = reward / 3 + ((Math.random() < (reward % 3 / 3f)) ? 1 : 0);
                if (count != 0) {
                    var self = (CrushingWheelControllerBlockEntity) (Object) this;
                    Vec3 centerPos = VecHelper.getCenterOf(self.getBlockPos());
                    Direction facing = self.getBlockState().getValue(CrushingWheelControllerBlock.FACING);
                    int offset = facing.getAxisDirection()
                            .getStep();
                    Vec3 outSpeed = new Vec3((facing.getAxis() == Direction.Axis.X ? 0.25D : 0.0D) * offset,
                            offset == 1 ? (facing.getAxis() == Direction.Axis.Y ? 0.5D : 0.0D) : 0.0D,
                            (facing.getAxis() == Direction.Axis.Z ? 0.25D : 0.0D) * offset);
                    Vec3 outPos = centerPos.add((facing.getAxis() == Direction.Axis.X ? .55f * offset : 0f),
                            (facing.getAxis() == Direction.Axis.Y ? .55f * offset : 0f), (facing.getAxis() == Direction.Axis.Z ? .55f * offset : 0f));
                    var expItem = new ItemEntity(processingEntity.level(), outPos.x(), outPos.y(), outPos.z(), new ItemStack(AllItems.EXP_NUGGET.get(), count));
                    expItem.setDeltaMovement(outSpeed);
                    expItem.getPersistentData()
                            .put("BypassCrushingWheel", NbtUtils.writeBlockPos(self.getBlockPos()));
                    processingEntity.level().addFreshEntity(expItem);
                }
            }
        }
    }
}
