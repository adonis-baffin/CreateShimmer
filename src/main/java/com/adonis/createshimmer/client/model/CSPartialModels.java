package com.adonis.createshimmer.client.model;

import com.adonis.createshimmer.common.CSCommon;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CSPartialModels {
    public static final PartialModel MECHANICAL_GRINDSTONE = block("mechanical_grindstone");

    public static void register() {}

    private static PartialModel block(String path) {
        return PartialModel.of(CSCommon.asResource("block/" + path));
    }
}
