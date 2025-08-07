package com.adonis.createshimmer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record CSIntIntPair(int level, int value) {
    public static final Codec<CSIntIntPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(CSIntIntPair::level),
            ExtraCodecs.POSITIVE_INT.fieldOf("value").forGetter(CSIntIntPair::value)).apply(instance, CSIntIntPair::new));
}
