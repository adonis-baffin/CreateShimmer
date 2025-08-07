package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSRepairConfig extends ConfigBase {
    public final ConfigBool enableScepterRepair = b(true, "enableScepterRepair", Comments.enableScepterRepair);
    public final ConfigInt scepterRepairCost = i(50, 1, 1000, "scepterRepairCost", Comments.scepterRepairCost);
    public final ConfigInt scepterRepairAmount = i(9, 1, 100, "scepterRepairAmount", Comments.scepterRepairAmount);
    public final ConfigBool debugMode = b(false, "debugMode", Comments.debugMode);

    @Override
    public String getName() {
        return "scepter_repair";
    }

    static class Comments {
        static final String enableScepterRepair = "Enable shimmer fluid repairing for Twilight Forest scepters";
        static final String scepterRepairCost = "Shimmer fluid cost per repair operation (in mB)";
        static final String scepterRepairAmount = "Durability points restored per repair operation";
        static final String debugMode = "Enable debug logging for scepter repair operations";
    }
}
