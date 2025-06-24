// CSDamageTypes.java
package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.CSCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CSDamageTypes {
    // 创建一个 DamageType 的 DeferredRegister
    public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, CSCommon.ID);

    // 定义我们自定义伤害类型的 ResourceKey
    public static final ResourceKey<DamageType> SHIMMER_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, CSCommon.asResource("shimmer_magic"));

    public static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);
    }
}
