package com.adonis.createshimmer.common.fluids.shimmer;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class ShimmerOpenPipeEffect implements OpenPipeEffectHandler {
    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        if (level.getGameTime() % 5 != 0)
            return;
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
        for (LivingEntity entity : entities) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false, false));
        }
    }
}
