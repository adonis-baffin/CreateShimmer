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
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import plus.dragons.createdragonsplus.common.CDPRegistrate;

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
        CSDamageTypes.register(modBus); // <-- 在这里添加这一行

        // --- 注册通用事件处理器 (服务端和客户端都运行) ---
        // ShimmerEventHandler 包含游戏逻辑（如伤害计算），必须在两端都注册。
        NeoForge.EVENT_BUS.register(ShimmerEffect.ShimmerEventHandler.class);

        modBus.register(this);
        modBus.register(new CSConfig(modContainer));
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        System.out.println("Create Shimmer setup complete!");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void register(final RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
//            CSAdvancements.register();
//            CSAdvancements.BuiltinTriggersQuickDeploy.register();
        }
    }

    public static ResourceLocation asResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(ID, name);
    }

    public static String asLocalization(String key) {
        return ID + "." + key;
    }
}