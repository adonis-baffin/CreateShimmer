package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.fan.glooming.GloomingFanProcessingType;
import com.adonis.createshimmer.common.kinetics.fan.transmutation.TransmutationFanProcessingType;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CSFanProcessingTypes {
    public static final ResourceKey<Registry<FanProcessingType>> REGISTRY_KEY = CreateRegistries.FAN_PROCESSING_TYPE;

    private static final DeferredRegister<FanProcessingType> TYPES = DeferredRegister.create(REGISTRY_KEY, CSCommon.ID);

    public static final DeferredHolder<FanProcessingType, TransmutationFanProcessingType> TRANSMUTATION = TYPES.register("transmutation", TransmutationFanProcessingType::new);
    public static final DeferredHolder<FanProcessingType, GloomingFanProcessingType> GLOOMING = TYPES.register("glooming", GloomingFanProcessingType::new);

    public static void register(IEventBus modBus) {
        TYPES.register(modBus);
    }
}
