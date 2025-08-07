package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSClientConfig extends ConfigBase {
    public final ConfigFloat experienceVisionMultiplier = f(1f, 1f, 256f,
            "experienceVisionMultiplier",
            Comments.experienceVisionMultiplier);

    // 微光效果FOV配置
    public final ConfigBool shimmerFovEnabled = b(true,
            "shimmerFovEnabled",
            Comments.shimmerFovEnabled);

    public final ConfigFloat shimmerFovMultiplier = f(0.85f, 0.1f, 2.0f,
            "shimmerFovMultiplier",
            Comments.shimmerFovMultiplier);

    @Override
    public String getName() {
        return "client";
    }

    static class Comments {
        static final String experienceVisionMultiplier = "The vision range through Liquid Experience will be multiplied by this factor";

        // 微光效果FOV注释
        static final String shimmerFovEnabled = "Enable FOV modification when player has shimmer effect";
        static final String shimmerFovMultiplier = "FOV multiplier for shimmer effect (lower = more zoomed in, higher = more zoomed out). Default: 0.85";
    }
}
