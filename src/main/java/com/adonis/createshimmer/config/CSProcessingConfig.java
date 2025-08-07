package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSProcessingConfig extends ConfigBase {
    public final ConfigFloat regularLightningStrikeTransformXpBlockChance = f(1, 0, 1,
            "regularLightningStrikeTransformXpBlockChance",
            CSProcessingConfig.Comments.regularLightningStrikeTransformXpBlockChance);

    @Override
    public String getName() {
        return "processing";
    }

    static class Comments {
        static final String regularLightningStrikeTransformXpBlockChance = "Probability of natural lightning strikes transforming Blocks of Experience.";
    }
}
