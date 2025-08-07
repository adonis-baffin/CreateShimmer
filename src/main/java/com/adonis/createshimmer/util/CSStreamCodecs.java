package com.adonis.createshimmer.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class CSStreamCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentInstance> ENCHANTMENT_INSTANCE = StreamCodec
            .composite(
                    Enchantment.STREAM_CODEC,
                    it -> it.enchantment,
                    ByteBufCodecs.VAR_INT,
                    it -> it.level,
                    EnchantmentInstance::new);
}
