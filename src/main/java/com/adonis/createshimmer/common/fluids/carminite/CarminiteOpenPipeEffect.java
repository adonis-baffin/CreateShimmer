package com.adonis.createshimmer.common.fluids.carminite;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class CarminiteOpenPipeEffect implements OpenPipeEffectHandler {
    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        // 砷铅铁溶液不给予任何效果
    }
}
