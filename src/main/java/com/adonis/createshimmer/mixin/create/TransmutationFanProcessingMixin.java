package com.adonis.createshimmer.mixin.create;

import com.adonis.createshimmer.common.registry.CSFanProcessingTypes;
import com.adonis.createshimmer.common.registry.CSFluids;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AirCurrent.class)
public class TransmutationFanProcessingMixin {
    @ModifyExpressionValue(method = "rebuild", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;getAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;"))
    private @Nullable FanProcessingType rebuild$checkShimmerFluid(
            @Nullable FanProcessingType original,
            @Local(name = "world") Level world,
            @Local(name = "currentPos") BlockPos currentPos) {
        // 如果已经找到了其他处理类型，不覆盖
        if (original != null) {
            return original;
        }

        var fluidState = world.getFluidState(currentPos);
        var catalystTag = CSFluids.MOD_TAGS.fanTransmutationCatalysts;

        // 检查流体是否在催化剂标签中
        if (fluidState.is(catalystTag)) {
            return CSFanProcessingTypes.TRANSMUTATION.get();
        }

        return original;
    }
}
