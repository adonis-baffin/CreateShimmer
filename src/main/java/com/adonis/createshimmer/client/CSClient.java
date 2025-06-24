package com.adonis.createshimmer.client;

import com.adonis.createshimmer.client.events.CSClientEvents;
import com.adonis.createshimmer.client.gui.ShimmerGui;
import com.adonis.createshimmer.client.model.CSPartialModels;
import com.adonis.createshimmer.common.CSCommon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber; // <--- 关键的 import
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// 使用独立的 @EventBusSubscriber 注解，而不是 @Mod.EventBusSubscriber
@EventBusSubscriber(modid = CSCommon.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CSClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 这个方法会在客户端加载时被自动调用
        CSPartialModels.register();

        // 注册所有只在客户端运行的事件处理器
        NeoForge.EVENT_BUS.register(new CSClientEvents());
        NeoForge.EVENT_BUS.register(new ShimmerGui());

        // 注意：ShimmerEffect.ShimmerEventHandler 已在 CSCommon 中注册，此处不再需要注册。
    }
}