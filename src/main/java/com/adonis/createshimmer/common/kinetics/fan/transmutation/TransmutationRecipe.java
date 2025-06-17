package com.adonis.createshimmer.common.kinetics.fan.transmutation;

import com.adonis.createshimmer.common.registry.CSRecipes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class TransmutationRecipe extends StandardProcessingRecipe<SingleRecipeInput> {
    public TransmutationRecipe(ProcessingRecipeParams params) {
        super(CSRecipes.TRANSMUTATION, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return getIngredients().getFirst().test(input.item());
    }

    public static StandardProcessingRecipe.Builder<TransmutationRecipe> builder(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(TransmutationRecipe::new, id);
    }
}