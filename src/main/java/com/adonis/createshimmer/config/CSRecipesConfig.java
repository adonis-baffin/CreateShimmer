/*
 * Copyright (C) 2025 DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSRecipesConfig extends ConfigBase {
    public final ConfigBool enableBulkTransmutation = b(true,
            "enableBulkTransmutation",
            Comments.enableBulkTransmutation);

    @Override
    public String getName() {
        return "recipes";
    }

    static class Comments {
        static final String enableBulkTransmutation = """
                Allow fans to process transmutation recipes when placed above transmutation catalysts.
                Transmutation catalysts include: shimmer fluids.""";
    }
}