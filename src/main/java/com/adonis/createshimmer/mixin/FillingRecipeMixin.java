package com.adonis.createshimmer.mixin;

import com.adonis.createshimmer.util.ScepterRepairHelper;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FillingRecipe.class, remap = false)
public class FillingRecipeMixin {
    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void onMatches(SingleRecipeInput inv, Level level, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = inv.getItem(0);
        FillingRecipe self = (FillingRecipe) (Object) this;

        // 优先处理权杖修复：精确匹配权杖类型
        if (ScepterRepairHelper.isRepairableScepter(stack)) {
            boolean isShimmerRepairRecipe = isShimmerRepairRecipe(self);

            if (isShimmerRepairRecipe) {
                // 这是权杖修复配方，检查权杖类型是否精确匹配
                boolean exactMatch = isExactScepterMatch(self, stack);
                boolean needsRepair = ScepterRepairHelper.needsRepair(stack);
                boolean matches = exactMatch && needsRepair;

                if (ScepterRepairHelper.isDebugMode()) {
                    System.out.println("Scepter repair recipe match check: " + matches +
                            " (exactMatch: " + exactMatch + ", needsRepair: " + needsRepair +
                            ", damage: " + stack.getDamageValue() +
                            ", item: " + stack.getItem() + ")");
                }

                cir.setReturnValue(matches);
                return;
            } else {
                // 这不是权杖修复配方，但输入是权杖，拒绝匹配
                if (ScepterRepairHelper.isDebugMode()) {
                    System.out.println("Preventing non-scepter recipe from matching scepter: " + stack.getItem());
                }
                cir.setReturnValue(false);
                return;
            }
        }

        // 对于非权杖的情况，检查是否是权杖修复配方
        if (isShimmerRepairRecipe(self)) {
            // 这是权杖修复配方，但输入不是权杖，拒绝匹配
            if (ScepterRepairHelper.isDebugMode()) {
                System.out.println("Preventing scepter repair recipe from matching non-scepter: " + stack.getItem());
            }
            cir.setReturnValue(false);
            return;
        }

        // 对于其他情况，使用原始逻辑
    }

    @Inject(method = "getRequiredFluid", at = @At("HEAD"), cancellable = true)
    private void onGetRequiredFluid(CallbackInfoReturnable<SizedFluidIngredient> cir) {
        FillingRecipe self = (FillingRecipe) (Object) this;

        // 检查是否是权杖修复配方且缺少流体成分
        if (self.getFluidIngredients().isEmpty() && isShimmerRepairRecipeByItems(self)) {
            try {
                // 创建微光流体需求
                net.minecraft.world.level.material.Fluid shimmerFluid = getShimmerFluid();
                if (shimmerFluid != null) {
                    // 从配方输入取一个示例 ItemStack（假设第一个是权杖）
                    ItemStack exampleScepter = self.getIngredients().get(0).getItems()[0];
                    FluidStack shimmerStack = new FluidStack(shimmerFluid, ScepterRepairHelper.getRepairCost(exampleScepter));  // 传入 exampleScepter

                    SizedFluidIngredient shimmerIngredient = SizedFluidIngredient.of(shimmerStack);

                    if (ScepterRepairHelper.isDebugMode()) {
                        System.out.println("Created shimmer fluid ingredient for recipe: " + ScepterRepairHelper.getRepairCost(exampleScepter) + "mB");
                    }

                    cir.setReturnValue(shimmerIngredient);
                    return;
                }
            } catch (Exception e) {
                if (ScepterRepairHelper.isDebugMode()) {
                    System.err.println("Error creating shimmer fluid ingredient: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 检查权杖类型是否与配方精确匹配
     */
    private boolean isExactScepterMatch(FillingRecipe recipe, ItemStack inputStack) {
        try {
            // 检查配方的输入成分
            var ingredients = recipe.getIngredients();
            if (!ingredients.isEmpty()) {
                var ingredient = ingredients.get(0);
                var items = ingredient.getItems();

                // 检查是否有完全匹配的物品类型
                for (ItemStack recipeItem : items) {
                    if (recipeItem.getItem() == inputStack.getItem()) {
                        if (ScepterRepairHelper.isDebugMode()) {
                            System.out.println("Exact scepter match found: " + inputStack.getItem() + " matches recipe item: " + recipeItem.getItem());
                        }
                        return true;
                    }
                }

                if (ScepterRepairHelper.isDebugMode()) {
                    System.out.println("No exact match for " + inputStack.getItem() + " in recipe with items: " +
                            java.util.Arrays.toString(items));
                }
            }
        } catch (Exception e) {
            if (ScepterRepairHelper.isDebugMode()) {
                System.err.println("Error checking exact scepter match: " + e.getMessage());
            }
        }
        return false;
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

    private boolean isShimmerRepairRecipeByItems(FillingRecipe recipe) {
        try {
            // 检查配方的输入是否是权杖
            var ingredients = recipe.getIngredients();
            if (!ingredients.isEmpty()) {
                var ingredient = ingredients.get(0);
                var items = ingredient.getItems();

                for (ItemStack item : items) {
                    if (ScepterRepairHelper.isRepairableScepter(item)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            if (ScepterRepairHelper.isDebugMode()) {
                System.err.println("Error checking shimmer repair recipe by items: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 获取微光流体
     */
    private net.minecraft.world.level.material.Fluid getShimmerFluid() {
        try {
            return net.minecraft.core.registries.BuiltInRegistries.FLUID
                    .get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("create_shimmer", "shimmer"));
        } catch (Exception e) {
            if (ScepterRepairHelper.isDebugMode()) {
                System.err.println("Error getting shimmer fluid: " + e.getMessage());
            }
            return null;
        }
    }
}