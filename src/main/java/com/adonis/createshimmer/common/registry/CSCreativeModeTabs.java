
package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.registry.CSBlocks.*;
import static com.adonis.createshimmer.common.registry.CSItems.*;
import static com.simibubi.create.AllBlocks.EXPERIENCE_BLOCK;
import static com.simibubi.create.AllItems.EXP_NUGGET;
import static plus.dragons.createdragonsplus.common.registry.CDPItems.BLAZE_UPGRADE_SMITHING_TEMPLATE;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.util.CSLang;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CSCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, CSCommon.ID);
    public static final Holder<CreativeModeTab> BASE = TABS.register("base", CSCreativeModeTabs::base);

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }

    private static CreativeModeTab base(ResourceLocation id) {
        return CreativeModeTab.builder()
                .title(CSLang.description("itemGroup", id).component())
                .icon(SUPER_EXPERIENCE_NUGGET::asStack)
                .displayItems(CSCreativeModeTabs::buildBaseContents)
//                .withTabsBefore(ResourceLocation.fromNamespaceAndPath("create_dragons_plus", "base"))
                .build();
    }

    private static void buildBaseContents(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(MECHANICAL_GRINDSTONE);
        output.accept(SHIMMER_REAGENT);
        output.accept(EXPERIENCE_HATCH);
        output.accept(EXPERIENCE_LANTERN);
        output.accept(PRINTER);
        output.accept(BLAZE_ENCHANTER);
        output.accept(BLAZE_FORGER);
        output.accept(EXPERIENCE_BLOCK);
        output.accept(SUPER_EXPERIENCE_BLOCK);
        output.accept(EXP_NUGGET);
        output.accept(SUPER_EXPERIENCE_NUGGET);
        output.accept(ENCHANTING_TEMPLATE);
        output.accept(SUPER_ENCHANTING_TEMPLATE);
        output.accept(BLAZE_UPGRADE_SMITHING_TEMPLATE);
        output.accept(EXPERIENCE_CAKE_BASE, TabVisibility.SEARCH_TAB_ONLY);
        output.accept(EXPERIENCE_CAKE);
        output.accept(EXPERIENCE_CAKE_SLICE);
        output.accept(EXPERIENCE_BUCKET);
    }
}
