package com.adonis.createshimmer.common;

import com.adonis.createshimmer.common.effects.ShimmerEffect;
import com.adonis.createshimmer.common.registry.*;
import com.adonis.createshimmer.config.CSConfig;
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
import net.neoforged.fml.ModList;  // 新导入：用于检查 mod 是否加载
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import plus.dragons.createdragonsplus.common.CDPRegistrate;
import com.adonis.createshimmer.compat.curios.CuriosCompat;  // 保持导入，但条件调用

@Mod(CSCommon.ID)
public class CSCommon {
    public static final String ID = "create_shimmer";
    public static final CDPRegistrate REGISTRATE = new CDPRegistrate(ID)
            .setTooltipModifier(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    public CSCommon(IEventBus modBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modBus);
        CSFluids.register(modBus);
        CSBlocks.register(modBus);
//        CSBlockEntities.register(modBus);
        CSItems.register(modBus);
        CSCreativeModeTabs.register(modBus);
        CSRecipes.register(modBus);
        CSEnchantments.register(modBus);
        CSArmInterationPoints.register(modBus);
        CSDataMaps.register(modBus);
        CSStats.register(modBus);
        CSMountedStorageTypes.register(modBus);
        CSFanProcessingTypes.register(modBus);
        CSEffects.register(modBus);
        CSDamageTypes.register(modBus);

        // 注册所有事件处理器
        NeoForge.EVENT_BUS.register(ShimmerEffect.ShimmerEventHandler.class);  // 微光效果主要处理器

        CSConfig config = new CSConfig(modContainer);

        modBus.register(this);
        modBus.register(config);

        // 添加 Curios 兼容事件监听 - 使用 lambda 条件化，避免提前加载
        modBus.addListener(RegisterCapabilitiesEvent.class, event -> {
            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.registerCapabilities(event);
            }
        });
        modBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, event -> {
            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.registerLayers(event);
            }
        });
        modBus.addListener(FMLClientSetupEvent.class, event -> {
            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.registerRenderers(event);
            }
        });
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void register(final RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {}
    }

    public static ResourceLocation asResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(ID, name);
    }

    public static String asLocalization(String key) {
        return ID + "." + key;
    }
}