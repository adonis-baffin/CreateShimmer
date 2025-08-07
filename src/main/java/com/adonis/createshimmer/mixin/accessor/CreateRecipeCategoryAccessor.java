package com.adonis.createshimmer.mixin.accessor;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import java.util.List;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateRecipeCategory.class)
public interface CreateRecipeCategoryAccessor {
    @Invoker // Too lazy to copy the implementation myself XD
    static void invokeAddPotionTooltip(IRecipeSlotView view, List<Component> tooltip) {}
}
