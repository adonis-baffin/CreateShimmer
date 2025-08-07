package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CSServerConfig extends ConfigBase {
    public final CSKineticsConfig kinetics = nested(0, CSKineticsConfig::new, Comments.kinetics);
    public final CSFluidsConfig fluids = nested(0, CSFluidsConfig::new, Comments.fluids);
    public final CSEnchantmentsConfig enchantments = nested(0, CSEnchantmentsConfig::new, Comments.enchantments);
    public final CSProcessingConfig processing = nested(0, CSProcessingConfig::new, Comments.processing);
    public final CSRecipesConfig recipes = nested(0, CSRecipesConfig::new, Comments.recipes);
    public final CSRepairConfig repair = nested(0, CSRepairConfig::new, Comments.repair);

    @Override
    public void registerAll(ModConfigSpec.Builder builder) {
        super.registerAll(builder);
    }

    @Override
    public String getName() {
        return "server";
    }

    static class Comments {
        static final String kinetics = "Parameters and abilities of kinetic mechanisms";
        static final String fluids = "Parameters and abilities of fluids and fluid operating components";
        static final String enchantments = "Parameters and abilities of enchantment operating components";
        static final String processing = "Parameters and abilities of processing mechanisms and appliances";
        static final String recipes = "Parameters and abilities of recipe processing and fan-based bulk operations";
        static final String repair = "Parameters for shimmer-based scepter repair functionality";
    }
}
