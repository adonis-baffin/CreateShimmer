package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSRecipesConfig extends ConfigBase {
    public final ConfigBool enableBulkTransmutation = b(true,
            "enableBulkTransmutation",
            Comments.enableBulkTransmutation);

    // 新添加的批量通冥配置
    public final ConfigBool enableBulkGlooming = b(true,
            "enableBulkGlooming",
            Comments.enableBulkGlooming);

    @Override
    public String getName() {
        return "recipes";
    }

    static class Comments {
        static final String enableBulkTransmutation = """
                Allow fans to process transmutation recipes when placed above transmutation catalysts.
                Transmutation catalysts include: shimmer fluids.""";

        // 新添加的批量通冥注释
        static final String enableBulkGlooming = """
                Allow fans to process glooming recipes when placed above glooming catalyst blocks.
                Glooming catalysts include: Twilight Forest's ominous fire block.""";
    }
}