package com.adonis.createshimmer.mixin;

import com.adonis.createshimmer.util.ScepterRepairHelper;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ProcessingRecipe.class, remap = false)
public class ProcessingRecipeMixin {
    @Inject(method = "getResultItem", at = @At("HEAD"), cancellable = true)
    private void onGetResultItem(net.minecraft.core.HolderLookup.Provider registryAccess, CallbackInfoReturnable<ItemStack> cir) {
        // 获取实际的 ProcessingRecipe 实例 - 使用通配符避免泛型问题
        ProcessingRecipe<?, ?> recipe = (ProcessingRecipe<?, ?>) (Object) this;

        // 只处理 FillingRecipe 的权杖修复
        if (recipe instanceof FillingRecipe fillingRecipe) {
            // 如果是权杖修复配方，返回配方定义的结果物品（用于JEI显示）
            if (isShimmerRepairRecipe(fillingRecipe)) {
                // 使用配方自身定义的结果，而不是动态生成
                if (!fillingRecipe.getRollableResults().isEmpty()) {
                    ItemStack result = fillingRecipe.getRollableResults().get(0).getStack().copy();

                    if (ScepterRepairHelper.isDebugMode()) {
                        System.out.println("ProcessingRecipe - Returning recipe result for JEI: " + result.getItem());
                    }

                    cir.setReturnValue(result);
                    return;
                }
            }
        }
    }

    @Inject(method = "assemble", at = @At("HEAD"), cancellable = true)
    private void onAssemble(RecipeInput input, net.minecraft.core.HolderLookup.Provider registryAccess, CallbackInfoReturnable<ItemStack> cir) {
        // 获取实际的 ProcessingRecipe 实例
        ProcessingRecipe<?, ?> recipe = (ProcessingRecipe<?, ?>) (Object) this;

        // 只处理 FillingRecipe 的权杖修复
        if (recipe instanceof FillingRecipe fillingRecipe && input instanceof SingleRecipeInput singleInput) {
            ItemStack inputStack = singleInput.getItem(0);

            // 如果是权杖修复配方，返回修复后的权杖
            if (ScepterRepairHelper.isRepairableScepter(inputStack) &&
                    ScepterRepairHelper.needsRepair(inputStack) &&
                    isShimmerRepairRecipe(fillingRecipe)) {

                // 获取配方定义的结果物品作为基础
                ItemStack baseResult;
                if (!fillingRecipe.getRollableResults().isEmpty()) {
                    baseResult = fillingRecipe.getRollableResults().get(0).getStack().copy();
                } else {
                    // 如果配方没有定义结果，使用输入物品作为基础
                    baseResult = inputStack.copy();
                }

                // 确保结果物品类型与输入物品类型一致
                if (baseResult.getItem() != inputStack.getItem()) {
                    if (ScepterRepairHelper.isDebugMode()) {
                        System.out.println("ProcessingRecipe - Recipe result type mismatch! Input: " + inputStack.getItem() + ", Recipe result: " + baseResult.getItem());
                    }
                    baseResult = inputStack.copy();
                }

                // 应用修复逻辑
                ItemStack repairedScepter = ScepterRepairHelper.repairScepter(baseResult);

                if (ScepterRepairHelper.isDebugMode()) {
                    System.out.println("ProcessingRecipe - Assembling repaired scepter: " +
                            inputStack.getItem() + " (" + inputStack.getDamageValue() + " -> " + repairedScepter.getDamageValue() + ")");
                }

                cir.setReturnValue(repairedScepter);
                return;
            }
        }
    }

    private boolean isShimmerRepairRecipe(FillingRecipe recipe) {
        try {
            boolean hasScepterInput = false;
            // 先检查输入是否是权杖
            var ingredients = recipe.getIngredients();
            if (!ingredients.isEmpty()) {
                var ingredient = ingredients.get(0);
                var items = ingredient.getItems();

                for (ItemStack item : items) {
                    if (ScepterRepairHelper.isRepairableScepter(item)) {
                        hasScepterInput = true;
                        break;
                    }
                }
            }

            // 如果输入不是权杖，直接返回 false（不是 repair 配方）
            if (!hasScepterInput) {
                return false;
            }

            // 再检查流体是否是微光
            if (!recipe.getFluidIngredients().isEmpty()) {
                SizedFluidIngredient sizedFluidIngredient = recipe.getFluidIngredients().get(0);
                FluidIngredient fluidIngredient = sizedFluidIngredient.ingredient();
                FluidStack[] matchingFluids = fluidIngredient.getStacks();

                for (var fluidStack : matchingFluids) {
                    ResourceLocation fluidName = BuiltInRegistries.FLUID.getKey(fluidStack.getFluid());
                    if (fluidName != null && fluidName.toString().contains("shimmer")) {
                        return true;
                    }
                }
            }

            // 如果没有流体成分，但输入是权杖，也认为是 repair（根据你的原逻辑）
            return true;
        } catch (Exception e) {
            if (ScepterRepairHelper.isDebugMode()) {
                System.err.println("Error checking shimmer repair recipe: " + e.getMessage());
            }
        }
        return false;
    }
}
