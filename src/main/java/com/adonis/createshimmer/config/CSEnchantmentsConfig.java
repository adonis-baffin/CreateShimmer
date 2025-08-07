package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSEnchantmentsConfig extends ConfigBase {
    public final ConfigInt blazeEnchanterMaxEnchantLevel = i(30, 0,
            "blazeEnchanterMaxEnchantLevel",
            Comments.blazeEnchanterMaxEnchantLevel);
    public final ConfigInt blazeEnchanterMaxSuperEnchantLevel = i(60, 0,
            "blazeEnchanterMaxSuperEnchantLevel",
            Comments.blazeEnchanterMaxSuperEnchantLevel);
    public final ConfigInt enchantmentMaxLevelExtension = i(1, 0, 255,
            "enchantmentMaxLevelExtension",
            Comments.enchantmentMaxLevelExtension);
    public final ConfigBool ignoreEnchantmentCompatibility = b(true,
            "ignoreEnchantmentCompatibility",
            Comments.ignoreEnchantmentCompatibility);
    public final ConfigBool splitEnchantmentRespectLevelExtension = b(false,
            "splitEnchantmentRespectLevelExtension",
            Comments.splitEnchantmentRespectLevelExtension);

    @Override
    public String getName() {
        return "enchantments";
    }

    static class Comments {
        static final String blazeEnchanterMaxEnchantLevel = "The max experience level a Blaze Enchanter can use in Regular Enchanting";
        static final String blazeEnchanterMaxSuperEnchantLevel = "The max experience level a Blaze Enchanter can use in Super Enchanting";
        static final String enchantmentMaxLevelExtension = "Max enchantment level in Super Enchanting will be extended by this value";
        static final String ignoreEnchantmentCompatibility = "If Super Enchanting and Super Forging ignores enchantment compatibility";
        static final String splitEnchantmentRespectLevelExtension = "If Enchantment splitting respects over-capped level";
    }
}
