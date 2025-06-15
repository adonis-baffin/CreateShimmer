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

package plus.dragons.createenchantmentindustry.common;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import plus.dragons.createdragonsplus.common.CDPRegistrate;
import plus.dragons.createenchantmentindustry.common.registry.*;
//import plus.dragons.createenchantmentindustry.common.registry.CEIAdvancements;
import plus.dragons.createenchantmentindustry.config.CEIConfig;

@Mod(CEICommon.ID)
public class CEICommon {
    public static final String ID = "create_enchantment_industry";
    public static final CDPRegistrate REGISTRATE = new CDPRegistrate(ID)
            .setTooltipModifier(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    public CEICommon(IEventBus modBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modBus);
        CEIFluids.register(modBus);
        CEIBlocks.register(modBus);
        CEIBlockEntities.register(modBus);
        CEIItems.register(modBus);
        CEICreativeModeTabs.register(modBus);
        CEIRecipes.register(modBus);
//        CEIEnchantments.register(modBus);
//        CEIArmInterationPoints.register(modBus);
//        CEIDataMaps.register(modBus);
//        CEIStats.register(modBus);
//        CEIMountedStorageTypes.register(modBus);
        modBus.register(this);
        modBus.register(new CEIConfig(modContainer));
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void register(final RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
//            CEIAdvancements.register();
//            CEIAdvancements.BuiltinTriggersQuickDeploy.register();
        }
    }

    public static ResourceLocation asResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(ID, name);
    }

    public static String asLocalization(String key) {
        return ID + "." + key;
    }

    // TODO Add more Option to customize printer behavior
}
