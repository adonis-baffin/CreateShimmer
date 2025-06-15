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
//package plus.dragons.createenchantmentindustry.mixin;
//
//import com.llamalad7.mixinextras.sugar.Local;
//import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
//import java.util.ArrayList;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.neoforged.neoforge.common.CommonHooks;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(DeployerHandler.class)
//public class DeployerHandlerMixin {
//    @Redirect(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
//    private static void tryHarvestBlock$postBlockDropsEvent(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience, @Local(argsOnly = true) ServerPlayer player, @Local BlockEntity blockEntity) {
//        CommonHooks.handleBlockDrops(level, pos, state, blockEntity, new ArrayList<>(), player, stack);
//    }
//}
