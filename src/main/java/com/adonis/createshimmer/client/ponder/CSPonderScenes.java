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

package com.adonis.createshimmer.client.ponder;

import com.adonis.createshimmer.client.ponder.scene.*;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public class CSPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(AllItems.EXP_NUGGET)
                .addStoryBoard("experience/basic", ExperienceScene::basic, CSPonderTags.EXPERIENCE_APPLIANCES)
                .addStoryBoard("experience/advance", ExperienceScene::advance, CSPonderTags.SUPER_EXPERIENCE_APPLIANCES)
                .addStoryBoard("experience/prepare_for_super_enchant", ExperienceScene::prepare)
                .addStoryBoard("experience/beacon_base", ExperienceScene::beaconBase);

        HELPER.forComponents(CSBlocks.EXPERIENCE_HATCH)
                .addStoryBoard("experience_hatch", MiscScene::experienceHatch, CSPonderTags.EXPERIENCE_APPLIANCES);

        HELPER.forComponents(CSBlocks.MECHANICAL_GRINDSTONE)
                .addStoryBoard("grindstone/basic", GrindstoneScene::basic, CSPonderTags.EXPERIENCE_APPLIANCES)
                .addStoryBoard("grindstone/extra", GrindstoneScene::extra);

        HELPER.forComponents(CSBlocks.BLAZE_ENCHANTER)
                .addStoryBoard("enchanter", EnchanterScene::basic, CSPonderTags.EXPERIENCE_APPLIANCES)
                .addStoryBoard("enchanter", EnchanterScene::superEnchant, CSPonderTags.SUPER_EXPERIENCE_APPLIANCES)
                .addStoryBoard("automate_enchanter", EnchanterScene::automate, AllCreatePonderTags.ARM_TARGETS);

        HELPER.forComponents(CSBlocks.BLAZE_FORGER)
                .addStoryBoard("forger", ForgerScene::basic, CSPonderTags.EXPERIENCE_APPLIANCES)
                .addStoryBoard("forger", ForgerScene::superEnchant, CSPonderTags.SUPER_EXPERIENCE_APPLIANCES)
                .addStoryBoard("automate_forger", ForgerScene::automate, AllCreatePonderTags.ARM_TARGETS);

        HELPER.forComponents(CSBlocks.PRINTER)
                .addStoryBoard("printer", MiscScene::printer, CSPonderTags.EXPERIENCE_APPLIANCES);

        HELPER.forComponents(CSBlocks.EXPERIENCE_LANTERN)
                .addStoryBoard("experience_lantern", MiscScene::experienceLantern, CSPonderTags.EXPERIENCE_APPLIANCES, AllCreatePonderTags.CONTRAPTION_ACTOR);
    }

    public static void enchant(CreateSceneBuilder scene, ItemStack item, ResourceKey<Enchantment> enchantment, int level) {
        if (DatagenModLoader.isRunningDataGen()) // scene.world().getHolderLookupProvider() cause null when get level
            return;
        var e = scene.world().getHolderLookupProvider()
                .lookup(Registries.ENCHANTMENT)
                .get().getOrThrow(enchantment);
        item.enchant(e, level);
    }
}
