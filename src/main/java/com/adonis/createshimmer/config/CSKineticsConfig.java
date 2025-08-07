package com.adonis.createshimmer.config;

import net.createmod.catnip.config.ConfigBase;

public class CSKineticsConfig extends ConfigBase {
    public final ConfigBool deployerKillDropXp = b(true, "deployerKillDropXp", Comments.deployerKillDropXp);
    public final ConfigFloat deployerKillXpScale = f(1, 0, 1, "deployerKillXpScale", Comments.deployerKillXpScale);
    public final ConfigBool deployerMineDropXp = b(true, "deployerMineDropXp", Comments.deployerMineDropXp);
    public final ConfigFloat deployerMineXpScale = f(1, 0, 1, "deployerMineXpScale", Comments.deployerMineXpScale);
    public final ConfigBool deployerCollectXp = b(true, "deployerCollectXp", Comments.deployerCollectXp);
    public final ConfigBool deployerMendItem = b(true, "deployerMendItem", Comments.deployerMendItem);
    public final ConfigBool deployerSweepAttack = b(true, "deployerSweepAttack", Comments.deployerSweepAttack);
    public final ConfigBool crushingWheelKillDropXp = b(true, "crushingWheelKillDropXp", Comments.crushingWheelKillDropXp);
    public final ConfigFloat crushingWheelKillDropXpChance = f(0.3f, 0, 1, "crushingWheelKillDropXpChance", Comments.crushingWheelKillDropXpChance);
    public final ConfigFloat crushingWheelKillDropXpScale = f(0.34f, 0, 1, "crushingWheelKillDropXpScale", Comments.crushingWheelKillDropXpScale);
    public final CSStressConfig stressValues = nested(0, CSStressConfig::new, Comments.stress);

    @Override
    public String getName() {
        return "kinetics";
    }

    static class Comments {
        static final String stress = "Fine tune the kinetic stats of individual components";
        static final String deployerKillDropXp = "Whether Deployer-killed entities should drop experience.";
        static final String deployerKillXpScale = "Scale for experience dropped from Deployer-killed entities.";
        static final String deployerMineDropXp = "Whether Deployer-mined blocks should drop experience.";
        static final String deployerMineXpScale = "Scale for experience dropped from Deployer-mined blocks.";
        static final String deployerCollectXp = "Whether Deployers collect dropped experience as Nuggets of Experience.";
        static final String deployerMendItem = "Whether the Mending enchantment applies to Deployer-held items (Needs deployerCollectXp = true).";
        static final String deployerSweepAttack = "Whether Deployers can perform sweep attacks.";
        static final String crushingWheelKillDropXp = "Whether Crushing Wheel-killed entities should drop experience.";
        static final String crushingWheelKillDropXpChance = "Probability of Crushing Wheel-killed entities dropping experience.";
        static final String crushingWheelKillDropXpScale = "Scale for experience dropped from Crushing Wheel-killed entities.";
    }
}
