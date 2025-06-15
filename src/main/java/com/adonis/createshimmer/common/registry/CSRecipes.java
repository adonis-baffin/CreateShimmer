
package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.fluids.printer.PrintingRecipe;
import com.adonis.createshimmer.common.kinetics.grindstone.GrindingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import plus.dragons.createdragonsplus.common.recipe.RecipeTypeInfo;

public class CSRecipes {
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, CSCommon.ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CSCommon.ID);
    public static final RecipeTypeInfo<PrintingRecipe> PRINTING = register("printing", () -> new PrintingRecipe.Serializer<>(PrintingRecipe::new));
    public static final RecipeTypeInfo<GrindingRecipe> GRINDING = register("grinding", () -> new StandardProcessingRecipe.Serializer<>(GrindingRecipe::new));

    public static void register(IEventBus modBus) {
        TYPES.register(modBus);
        SERIALIZERS.register(modBus);
    }

    private static <R extends Recipe<?>> RecipeTypeInfo<R> register(String name, Supplier<? extends RecipeSerializer<R>> serializer) {
        return new RecipeTypeInfo<>(name, serializer, SERIALIZERS, TYPES);
    }
}
