package com.adonis.createshimmer.client.model;

import com.adonis.createshimmer.common.CSCommon;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CSPartialModels {
    public static final PartialModel MECHANICAL_GRINDSTONE = block("mechanical_grindstone");
    public static final PartialModel PRINTER_NOZZLE_TOP = block("printer/nozzle_top");
    public static final PartialModel PRINTER_NOZZLE_BOTTOM = block("printer/nozzle_bottom");
    public static final PartialModel PRINTER_PISTON = block("printer/piston");
    public static final PartialModel BLAZE_ENCHANTER_HAT = block("blaze/enchanter_hat");
    public static final PartialModel BLAZE_ENCHANTER_HAT_SMALL = block("blaze/enchanter_hat_small");
    public static final PartialModel BLAZE_FORGER_HAT = block("blaze/forger_hat");
    public static final PartialModel BLAZE_FORGER_HAT_SMALL = block("blaze/forger_hat_small");

    public static void register() {}

    private static PartialModel block(String path) {
        return PartialModel.of(CSCommon.asResource("block/" + path));
    }
}
