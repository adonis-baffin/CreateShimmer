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

package plus.dragons.createenchantmentindustry.mixin;

import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plus.dragons.createenchantmentindustry.config.CEIConfig;

@Mixin(value = DeployerFakePlayer.class)
public class DeployerFakePlayerMixin {
    @Inject(method = "deployerKillsDoNotSpawnXP", at = @At("HEAD"), cancellable = true)
    private static void deployerKillsDoNotSpawnXP$lowerPriority(LivingExperienceDropEvent event, CallbackInfo ci) {
        if (CEIConfig.kinetics().deployerKillDropXp.get())
            ci.cancel();
    }
}
