package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSRepairConfig extends ConfigBase {
    public final ConfigBool enableScepterRepair = b(true, "enableScepterRepair", Comments.enableScepterRepair);

    // 四个独立的修复成本配置
    public final ConfigInt twilightScepterRepairCost = i(500, 1, 1000, "twilightScepterRepairCost", Comments.twilightScepterRepairCost);
    public final ConfigInt lifedrainScepterRepairCost = i(500, 1, 1000, "lifedrainScepterRepairCost", Comments.lifedrainScepterRepairCost);
    public final ConfigInt zombieScepterRepairCost = i(50, 1, 1000, "zombieScepterRepairCost", Comments.zombieScepterRepairCost);
    public final ConfigInt fortificationScepterRepairCost = i(50, 1, 1000, "fortificationScepterRepairCost", Comments.fortificationScepterRepairCost);

    public final ConfigInt scepterRepairAmount = i(9, 1, 100, "scepterRepairAmount", Comments.scepterRepairAmount);
    public final ConfigBool debugMode = b(false, "debugMode", Comments.debugMode);

    @Override
    public String getName() {
        return "scepter_repair";
    }

    static class Comments {
        static final String enableScepterRepair = "Enable shimmer fluid repairing for Twilight Forest scepters";
        static final String twilightScepterRepairCost = "Shimmer fluid cost per repair operation for Twilight Scepter (in mB)";
        static final String lifedrainScepterRepairCost = "Shimmer fluid cost per repair operation for Lifedrain Scepter (in mB)";
        static final String zombieScepterRepairCost = "Shimmer fluid cost per repair operation for Zombie Scepter (in mB)";
        static final String fortificationScepterRepairCost = "Shimmer fluid cost per repair operation for Fortification Scepter (in mB)";
        static final String scepterRepairAmount = "Durability points restored per repair operation";
        static final String debugMode = "Enable debug logging for scepter repair operations";
    }
}
