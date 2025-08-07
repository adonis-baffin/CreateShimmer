package com.adonis.createshimmer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class CSCodecs {
    public static final Codec<EnchantmentInstance> ENCHANTMENT_INSTANCE = RecordCodecBuilder
            .create(instance -> instance.group(
                    Enchantment.CODEC.fieldOf("id").forGetter(it -> it.enchantment),
                    Codec.intRange(0, 255).fieldOf("level").forGetter(it -> it.level)).apply(instance, EnchantmentInstance::new));
}
