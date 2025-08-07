package com.adonis.createshimmer.config;

import net.minecraft.Util;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CSConfig {
    private static final CSClientConfig CLIENT_CONFIG = new CSClientConfig();
    private static final CSServerConfig SERVER_CONFIG = new CSServerConfig();
    private static ModConfigSpec CLIENT_SPEC;
    private static ModConfigSpec SERVER_SPEC;

    public CSConfig(ModContainer modContainer) {
        CLIENT_SPEC = Util.make(new ModConfigSpec.Builder().configure(builder -> {
            CLIENT_CONFIG.registerAll(builder);
            return Unit.INSTANCE;
        }).getValue(), spec -> modContainer.registerConfig(Type.CLIENT, spec));
        SERVER_SPEC = Util.make(new ModConfigSpec.Builder().configure(builder -> {
            SERVER_CONFIG.registerAll(builder);
            return Unit.INSTANCE;
        }).getValue(), spec -> modContainer.registerConfig(Type.SERVER, spec));
    }

    public static CSClientConfig client() {
        return CLIENT_CONFIG;
    }

    public static CSServerConfig server() {
        return SERVER_CONFIG;
    }

    public static CSKineticsConfig kinetics() {
        return SERVER_CONFIG.kinetics;
    }

    public static CSStressConfig stress() {
        return SERVER_CONFIG.kinetics.stressValues;
    }

    public static CSFluidsConfig fluids() {
        return SERVER_CONFIG.fluids;
    }

    public static CSEnchantmentsConfig enchantments() {
        return SERVER_CONFIG.enchantments;
    }

    public static CSProcessingConfig processing() {
        return SERVER_CONFIG.processing;
    }

    /**
     * 获取配方配置
     * 
     * @return CSRecipesConfig 配方配置实例
     */
    public static CSRecipesConfig recipes() {
        return SERVER_CONFIG.recipes;
    }

    @SubscribeEvent
    public void onLoad(ModConfigEvent.Loading event) {
        var spec = event.getConfig().getSpec();
        if (SERVER_SPEC == spec) {
            SERVER_CONFIG.onLoad();
        } else if (CLIENT_SPEC == spec) {
            CLIENT_CONFIG.onLoad();
        }
    }

    public static CSRepairConfig repair() {
        return server().repair;
    }

    @SubscribeEvent
    public void onReload(ModConfigEvent.Reloading event) {
        var spec = event.getConfig().getSpec();
        if (SERVER_SPEC == spec) {
            SERVER_CONFIG.onReload();
        } else if (CLIENT_SPEC == spec) {
            CLIENT_CONFIG.onReload();
        }
    }
}
