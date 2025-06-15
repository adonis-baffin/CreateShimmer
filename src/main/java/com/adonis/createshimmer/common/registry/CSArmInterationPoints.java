
package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.processing.enchanter.BlazeEnchanterArmInteractionPoint;
import com.adonis.createshimmer.common.processing.forger.BlazeForgerArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;

public class CSArmInterationPoints {
    public static final RegistryEntry<ArmInteractionPointType, BlazeEnchanterArmInteractionPoint.Type> BLAZE_ENCHANTER = REGISTRATE
            .armInteractionPoint("blaze_enchanter", BlazeEnchanterArmInteractionPoint.Type::new)
            .register();
    public static final RegistryEntry<ArmInteractionPointType, BlazeForgerArmInteractionPoint.Type> BLAZE_FORGER = REGISTRATE
            .armInteractionPoint("blaze_forger", BlazeForgerArmInteractionPoint.Type::new)
            .register();

    public static void register(IEventBus modBus) {}
}
