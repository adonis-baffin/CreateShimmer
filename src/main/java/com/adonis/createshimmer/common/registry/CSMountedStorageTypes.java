

package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.fluids.lantern.ExperienceLanternMountedFluidStorageType;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;

public class CSMountedStorageTypes {
    public static final RegistryEntry<MountedFluidStorageType<?>, ExperienceLanternMountedFluidStorageType> EXPERIENCE_LANTERN = REGISTRATE
            .mountedFluidStorage("experience_lantern", ExperienceLanternMountedFluidStorageType::new)
            .register();

    public static void register(IEventBus modBus) {}
}
