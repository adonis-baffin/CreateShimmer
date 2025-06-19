package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.registry.CSBlocks.*;
import static com.adonis.createshimmer.common.registry.CSItems.*;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.util.CSLang;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
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
                .icon(SHIMMER_REAGENT::asStack)
                .displayItems(CSCreativeModeTabs::buildBaseContents)
                .withTabsBefore(ResourceLocation.fromNamespaceAndPath("create_dragons_plus", "base"))
                .build();
    }

    private static void buildBaseContents(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(MAGIC_SOIL);
        output.accept(THORN_FLOUR);
        output.accept(RAW_KNIGHTMETAL);
        output.accept(SHIMMER_REAGENT);

        output.accept(SHIMMER_BUCKET);
        output.accept(CARMINITE_SOLUTION_BUCKET);
        output.accept(FIERY_TEAR_BUCKET);
    }
}
