package com.adonis.createshimmer.mixin;

import com.adonis.createshimmer.util.ScepterRepairHelper;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FillingBySpout.class, remap = false)
public class FillingBySpoutMixin {
    @Inject(method = "getRequiredAmountForItem", at = @At("HEAD"), cancellable = true)
    private static void onGetRequiredAmountForItem(Level level, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<Integer> cir) {
        // 对于权杖修复，返回所需的微光量
        if (ScepterRepairHelper.isRepairableScepter(stack) &&
                ScepterRepairHelper.needsRepair(stack) &&
                isShimmerFluid(availableFluid)) {

            int requiredAmount = ScepterRepairHelper.getRepairCost(stack);  // 传入 stack

            cir.setReturnValue(requiredAmount);
            return;
        }
        // 对于满耐久的权杖，返回 -1 表示不能处理
        else if (ScepterRepairHelper.isRepairableScepter(stack) &&
                !ScepterRepairHelper.needsRepair(stack) &&
                isShimmerFluid(availableFluid)) {

            cir.setReturnValue(-1); // 返回 -1 表示无法处理
            return;
        }
    }

    // 移除原有的 fillItem 注入，让配方系统处理
    // 这样可以确保流体被正确消耗，物品被正确替换

    /**
     * 检查是否是微光流体
     */
    private static boolean isShimmerFluid(FluidStack fluidStack) {
        if (fluidStack == null || fluidStack.isEmpty()) {
            return false;
        }

        String fluidName = fluidStack.getFluid().toString();
        boolean isShimmer = fluidName.contains("shimmer");

        return isShimmer;
    }
}