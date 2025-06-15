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
//import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.neoforged.neoforge.common.ItemAbilities;
//import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//import plus.dragons.createenchantmentindustry.config.CEIConfig;
//
//@Mixin(Player.class)
//public abstract class PlayerMixin extends LivingEntity {
//    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
//        super(entityType, level);
//    }
//
//    /**
//     * Inject before posting {@link SweepAttackEvent}, so subscribers get that the deployer's sweep attack is "vanilla".
//     */
//    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CommonHooks;fireSweepAttack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Z)Lnet/neoforged/neoforge/event/entity/player/SweepAttackEvent;"))
//    private boolean attack$allowDeployerSweepAttack(boolean flag) {
//        //noinspection ConstantValue
//        if (((Object) this) instanceof DeployerFakePlayer && CEIConfig.kinetics().deployerSweepAttack.get()) {
//            return this.getMainHandItem().canPerformAction(ItemAbilities.SWORD_SWEEP);
//        }
//        return flag;
//    }
//}
